package com.php25.userservice.server.service.impl;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.exception.ServiceException;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.service.ModelToDtoTransferable;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.userservice.server.dto.AdminMenuButtonDto;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.model.AdminMenuButton;
import com.php25.userservice.server.model.AdminRole;
import com.php25.userservice.server.model.RoleMenu;
import com.php25.userservice.server.repository.AdminRoleRepository;
import com.php25.userservice.server.repository.RoleMenuRepository;
import com.php25.userservice.server.service.AdminRoleService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@Slf4j
@Service
@Primary
@Transactional(rollbackFor = ServiceException.class)
public class AdminRoleServiceImpl implements AdminRoleService {

    private AdminRoleRepository adminRoleRepository;

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    private BaseServiceImpl<AdminRoleDto, AdminRole, Long> baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        baseService = new BaseServiceImpl<>(AdminRoleDto.class, AdminRole.class, adminRoleRepository);
    }

    @Autowired
    public void setAdminRoleRepository(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository;
    }

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }


    @Override
    public Optional<AdminRoleDto> save(AdminRoleDto obj) {
        Assert.notNull(obj, "adminRoleDto不能为null");
        Assert.notEmpty(obj.getMenus(), "adminRoleDto.menus至少需要包含一个元素");
        var adminRole = new AdminRole();
        BeanUtils.copyProperties(obj, adminRole);

        if (null == obj.getId() || "".equals(obj.getId())) {
            adminRole.setCreateTime(new Date());
        }
        var adminMenuButtons = obj.getMenus().stream().map(temp -> {
            AdminMenuButton adminMenuButton = new AdminMenuButton();
            BeanUtils.copyProperties(temp, adminMenuButton);
            return adminMenuButton;
        }).collect(Collectors.toList());
        adminRole.setUpdateTime(new Date());
        adminRole = adminRoleRepository.save(adminRole);
        for (var menuButton : adminMenuButtons) {
            var roleMenu = roleMenuRepository.findOneByRoleIdAndMenuId(adminRole.getId(), menuButton.getId());
            if (null == roleMenu) {
                roleMenu = new RoleMenu();
                roleMenu.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
                roleMenu.setAdminRole(adminRole);
                roleMenu.setAdminMenuButton(menuButton);
                roleMenuRepository.save(roleMenu);
            }
        }
        var adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.of(adminRoleDto);
    }


    @Override
    public Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids) {
        Assert.notEmpty((List<Long>) ids, "ids集合至少需要包含一个元素");
        return findAll(ids, true);
    }

    @Override
    public Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids, Boolean lazy) {
        Assert.notEmpty((List<Long>) ids, "ids集合至少需要包含一个元素");
        Assert.notNull(lazy, "lazy不能为null");
        var adminRoles = (List<AdminRole>) adminRoleRepository.findAllById(ids);
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            var adminRoleDto = new AdminRoleDto();
            if (lazy) {
                BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            } else {
                BeanUtils.copyProperties(adminRole, adminRoleDto);
                List<AdminMenuButtonDto> adminMenuButtonDtos = adminRole.getAdminMenuButtons().stream().map(adminMenuButton -> {
                    var adminMenuButtonDto = new AdminMenuButtonDto();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonDto);
                    return adminMenuButtonDto;
                }).collect(Collectors.toList());
                adminRoleDto.setMenus(adminMenuButtonDtos);
            }
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminRoleDto>> findAllEnabled() {
        var adminRoles = adminRoleRepository.findAllEnabled();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            var adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminRoleDto>> findAll() {
        var adminRoles = (List<AdminRole>) adminRoleRepository.findAll();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            var adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }


    @Override
    public Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        var userPage = baseService.query(pageNum, pageSize, searchParams, (adminRole, adminRoleDto) -> {
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
        }, Sort.Direction.DESC, "id");
        return userPage;
    }

    @Override
    public Optional<AdminRoleDto> findOne(Long id) {
        Assert.notNull(id, "id不能为null");
        var adminRole = adminRoleRepository.findById(id).orElse(null);
        if (null == adminRole) {
            return Optional.empty();
        }
        var adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.of(adminRoleDto);
    }

    @Override
    public Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, SearchParamBuilder searchParamBuilder, ModelToDtoTransferable modelToDtoTransferable, Sort sort) {
        return baseService.query(pageNum, pageSize, searchParamBuilder, modelToDtoTransferable, sort);
    }

    @Override
    public void softDelete(List<AdminRoleDto> adminRoleDtos) {
        baseService.softDelete(adminRoleDtos);
    }
}
