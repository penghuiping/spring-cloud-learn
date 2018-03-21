package com.joinsoft.userservice.server.service.impl;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.common.service.impl.BaseServiceImpl;
import com.joinsoft.common.specification.BaseSpecs;
import com.joinsoft.userservice.client.dto.AdminMenuButtonDto;
import com.joinsoft.userservice.client.dto.AdminRoleDto;
import com.joinsoft.userservice.server.model.AdminMenuButton;
import com.joinsoft.userservice.server.model.AdminRole;
import com.joinsoft.userservice.server.model.RoleMenu;
import com.joinsoft.userservice.server.repository.AdminRoleRepository;
import com.joinsoft.userservice.server.repository.RoleMenuRepository;
import com.joinsoft.userservice.server.service.AdminRoleService;
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
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@Transactional
@Service
@Primary
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleDto, AdminRole> implements AdminRoleService {

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
    public AdminRoleDto save(AdminRoleDto obj) {
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
        return adminRoleDto;
    }


    @Override
    public List<AdminRoleDto> findAll(Iterable<String> ids) {
        return findAll(ids, true);
    }


    public List<AdminRoleDto> findAll(Iterable<String> ids, Boolean lazy) {
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAll(ids);
        return adminRoles.parallelStream().map(adminRole -> {
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
        }).collect(Collectors.toList());
    }

    @Override
    public List<AdminRoleDto> findAllEnabled() {
        List<AdminRole> adminRoles = adminRoleRepository.findAllEnabled();
        return adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AdminRoleDto> findAll() {
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAll();
        return adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());
    }

    @Override
    public DataGridPageDto<AdminRoleDto> query(Integer pageNum, Integer pageSize, String searchParams) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<AdminRole> userPage = adminRoleRepository.findAll(BaseSpecs.<AdminRole>getSpecs(searchParams), pageRequest);
        List<AdminRoleDto> adminRoleBos = userPage.getContent().parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());

        PageImpl<AdminRoleDto> adminRolePage = new PageImpl<AdminRoleDto>(adminRoleBos, null, userPage.getTotalElements());
        return toDataGridPageDto(adminRolePage);
    }

    @Override
    public AdminRoleDto findOne(String id) {
        AdminRole adminRole = adminRoleRepository.findOne(id);
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return adminRoleDto;
    }

}
