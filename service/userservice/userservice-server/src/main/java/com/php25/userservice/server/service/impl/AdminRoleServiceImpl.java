package com.php25.userservice.server.service.impl;

import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecs;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.server.model.AdminMenuButton;
import com.php25.userservice.server.model.AdminRole;
import com.php25.userservice.server.model.RoleMenu;
import com.php25.userservice.server.repository.AdminRoleRepository;
import com.php25.userservice.server.repository.RoleMenuRepository;
import com.php25.userservice.server.service.AdminRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@Transactional
@Service
@Primary
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleDto, AdminRole, String> implements AdminRoleService {

    private AdminRoleRepository adminRoleRepository;

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    public void setAdminRoleRepository(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository;
        this.baseRepository = adminRoleRepository;
    }

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }


    @Override
    public Optional<AdminRoleDto> save(AdminRoleDto obj) {
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(obj, adminRole);
        if (null == obj.getMenus()) obj.setMenus(new ArrayList<>());
        if (null == obj.getId() || "".equals(obj.getId())) {
            adminRole.setCreateTime(new Date());
        }
        List<AdminMenuButton> adminMenuButtons = obj.getMenus().stream().map(temp -> {
            AdminMenuButton adminMenuButton = new AdminMenuButton();
            BeanUtils.copyProperties(temp, adminMenuButton);
            return adminMenuButton;
        }).collect(Collectors.toList());
        adminRole.setUpdateTime(new Date());
        adminRole = adminRoleRepository.save(adminRole);
        for (AdminMenuButton menuButton : adminMenuButtons) {
            RoleMenu roleMenu = roleMenuRepository.findOneByRoleIdAndMenuId(adminRole.getId(), menuButton.getId());
            if (null == roleMenu) {
                roleMenu = new RoleMenu();
                roleMenu.setAdminRole(adminRole);
                roleMenu.setAdminMenuButton(menuButton);
                roleMenuRepository.save(roleMenu);
            }
        }
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.ofNullable(adminRoleDto);
    }


    @Override
    public Optional<List<AdminRoleDto>> findAll(Iterable<String> ids) {
        return findAll(ids, true);
    }


    public Optional<List<AdminRoleDto>> findAll(Iterable<String> ids, Boolean lazy) {
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAll(ids);
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            if (lazy)
                BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            else {
                BeanUtils.copyProperties(adminRole, adminRoleDto);
                List<AdminMenuButtonDto> adminMenuButtonDtos = adminRole.getAdminMenuButtons().stream().map(adminMenuButton -> {
                    AdminMenuButtonDto adminMenuButtonDto = new AdminMenuButtonDto();
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
        List<AdminRole> adminRoles = adminRoleRepository.findAllEnabled();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminRoleDto>> findAll() {
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAll();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<AdminRole> userPage = adminRoleRepository.findAll(BaseSpecs.getSpecs(searchParams), pageRequest);
        List<AdminRoleDto> adminRoleBos = userPage.getContent().parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());

        PageImpl<AdminRoleDto> adminRolePage = new PageImpl<AdminRoleDto>(adminRoleBos, null, userPage.getTotalElements());
        return Optional.ofNullable(toDataGridPageDto(adminRolePage));
    }

    @Override
    public Optional<AdminRoleDto> findOne(String id) {
        AdminRole adminRole = adminRoleRepository.findOne(id);
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.ofNullable(adminRoleDto);
    }

}
