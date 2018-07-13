package com.php25.userservice.server.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecsFactory;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.dto.AdminUserDto;
import com.php25.userservice.server.model.AdminRole;
import com.php25.userservice.server.model.AdminUser;
import com.php25.userservice.server.model.UserRole;
import com.php25.userservice.server.repository.AdminUserRepository;
import com.php25.userservice.server.repository.UserRoleRepository;
import com.php25.userservice.server.service.AdminUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@com.alibaba.dubbo.config.annotation.Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
@Transactional
@Service
@Primary
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUserDto, AdminUser, Long> implements AdminUserService {

    private AdminUserRepository adminUserRepository;

    private UserRoleRepository userRoleRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    public void setAdminUserRepository(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
        this.baseRepository = adminUserRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<AdminUserDto> findOne(Long id) {
        Assert.notNull(id, "id不能为null");
        AdminUser adminUser = adminUserRepository.findById(id).orElse(null);
        if (null == adminUser) return Optional.empty();
        AdminUserDto adminUserDto = new AdminUserDto();
        BeanUtils.copyProperties(adminUser, adminUserDto, "roles");
        List<AdminRoleDto> adminRoleDtos = adminUser.getRoles().stream().map(role -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(role, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());
        adminUserDto.setRoles(adminRoleDtos);
        return Optional.ofNullable(adminUserDto);
    }

    public Optional<List<AdminUserDto>> findAll(Iterable<Long> ids) {
        return findAll(ids, true);
    }

    public Optional<List<AdminUserDto>> findAll(Iterable<Long> ids, Boolean lazy) {
        Assert.notEmpty(Lists.newArrayList(ids), "ids至少需要一个元素");
        Assert.notNull(lazy, "lazy需要填入true或者false");
        return Optional.ofNullable(Lists.newArrayList(adminUserRepository.findAllById(ids)).stream().map(adminUser -> {
            AdminUserDto temp = new AdminUserDto();
            if (lazy)
                BeanUtils.copyProperties(adminUser, temp, "roles", "menus");
            else
                BeanUtils.copyProperties(adminUser, temp);
            return temp;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<AdminUserDto> save(AdminUserDto obj) {
        Assert.notNull(obj, "adminUserDto不能为null");
        Assert.notEmpty(obj.getRoles(), "adminUserDto.roles不能为null,且必须要有一个元素");
        Long id = obj.getId();
        if (null != id && id > 0) {
            obj.setUpdateTime(new Date());
            AdminUser adminUser = new AdminUser();
            BeanUtils.copyProperties(obj, adminUser, "roles");
            List<AdminRole> adminRoles = obj.getRoles().stream().map(role -> {
                AdminRole adminRole = new AdminRole();
                BeanUtils.copyProperties(role, adminRole, "adminMenuButtons");
                return adminRole;
            }).collect(Collectors.toList());
            adminUserRepository.save(adminUser);
            for (AdminRole adminRole : adminRoles) {
                UserRole userRole = userRoleRepository.findOneByRoleIdAndUserId(adminRole.getId(), adminUser.getId());
                if (null == userRole) {
                    userRole = new UserRole();
                    userRole.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
                    userRole.setAdminUser(adminUser);
                    userRole.setAdminRole(adminRole);
                    userRoleRepository.save(userRole);
                }
            }
            return Optional.ofNullable(obj);
        } else {
            obj.setCreateTime(new Date());
            obj.setUpdateTime(new Date());
            obj.setPassword(obj.getPassword());
            AdminUser adminUser = new AdminUser();
            BeanUtils.copyProperties(obj, adminUser, "roles");
            List<AdminRole> adminRoles = obj.getRoles().stream().map(role -> {
                AdminRole adminRole = new AdminRole();
                BeanUtils.copyProperties(role, adminRole, "adminMenuButtons");
                return adminRole;
            }).collect(Collectors.toList());
            adminUser = adminUserRepository.save(adminUser);
            for (AdminRole adminRole : adminRoles) {
                UserRole userRole = new UserRole();
                userRole.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
                userRole.setAdminUser(adminUser);
                userRole.setAdminRole(adminRole);
                userRoleRepository.save(userRole);
            }
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUser, adminUserDto);
            return Optional.ofNullable(adminUserDto);
        }
    }


    @Override
    public Optional<DataGridPageDto<AdminUserDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        Assert.notNull(pageNum, "pageNum不能为null");
        Assert.notNull(pageSize, "pageSize不能为null");
        Assert.hasText(searchParams, "searchParams不能为空,如没有搜索条件请使用[]");
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<AdminUser> userPage = adminUserRepository.findAll(BaseSpecsFactory.getJpaInstance().getSpecs(searchParams), pageRequest);
        List<AdminUserDto> adminUserBos = userPage.getContent().stream().map(adminUser -> {
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUser, adminUserDto, "roles");
            List<AdminRoleDto> adminRoleDtos = adminUser.getRoles().stream().map(role -> {
                AdminRoleDto adminRoleDto = new AdminRoleDto();
                BeanUtils.copyProperties(role, adminRoleDto, "adminMenuButtons");
                return adminRoleDto;
            }).collect(Collectors.toList());
            adminUserDto.setRoles(adminRoleDtos);
            return adminUserDto;
        }).collect(Collectors.toList());
        PageImpl<AdminUserDto> adminUserPage = new PageImpl<AdminUserDto>(adminUserBos, null, userPage.getTotalElements());
        return Optional.ofNullable(toDataGridPageDto(adminUserPage));
    }

    @Override
    public Optional<AdminUserDto> findByLoginNameAndPassword(String loginName, String password) {
        Assert.hasText(loginName, "loginName不能为空");
        Assert.hasText(loginName, "password不能为空");
        AdminUser adminUser = adminUserRepository.findByLoginNameAndPassword(loginName, password);
        if (null != adminUser) {
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUser, adminUserDto, "roles");
            List<AdminRoleDto> adminRoleDtos = adminUser.getRoles().stream().map(role -> {
                AdminRoleDto adminRoleDto = new AdminRoleDto();
                BeanUtils.copyProperties(role, adminRoleDto, "adminMenuButtons");
                return adminRoleDto;
            }).collect(Collectors.toList());
            adminUserDto.setRoles(adminRoleDtos);
            return Optional.ofNullable(adminUserDto);
        } else
            return Optional.empty();
    }
}
