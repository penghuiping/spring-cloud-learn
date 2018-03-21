package com.joinsoft.userservice.server.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecs;
import com.joinsoft.userservice.client.dto.AdminRoleDto;
import com.joinsoft.userservice.client.dto.AdminUserDto;
import com.joinsoft.userservice.server.model.AdminRole;
import com.joinsoft.userservice.server.model.AdminUser;
import com.joinsoft.userservice.server.model.UserRole;
import com.joinsoft.userservice.server.repository.AdminUserRepository;
import com.joinsoft.userservice.server.repository.UserRoleRepository;
import com.joinsoft.userservice.server.service.AdminUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@Transactional
@Service
@Primary
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUserDto, AdminUser> implements AdminUserService {

    private AdminUserRepository adminUserRepository;

    private UserRoleRepository userRoleRepository;

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
    public AdminUserDto findOne(String id) {
        AdminUser adminUser = adminUserRepository.findOne(id);
        AdminUserDto adminUserDto = new AdminUserDto();
        BeanUtils.copyProperties(adminUser, adminUserDto, "roles");
        List<AdminRoleDto> adminRoleDtos = adminUser.getRoles().stream().map(role -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(role, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());
        adminUserDto.setRoles(adminRoleDtos);
        return adminUserDto;
    }

    public List<AdminUserDto> findAll(Iterable<String> ids) {
        return findAll(ids, true);
    }

    public List<AdminUserDto> findAll(Iterable<String> ids, Boolean lazy) {
        return Lists.newArrayList(adminUserRepository.findAll(ids)).stream().map(adminUser -> {
            AdminUserDto temp = new AdminUserDto();
            if (lazy)
                BeanUtils.copyProperties(adminUser, temp, "roles", "menus");
            else
                BeanUtils.copyProperties(adminUser, temp);
            return temp;
        }).collect(Collectors.toList());
    }

    @Override
    public AdminUserDto save(AdminUserDto obj) {
        String id = obj.getId();
        if (null != id && id.length() > 0) {
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
                    userRole.setAdminUser(adminUser);
                    userRole.setAdminRole(adminRole);
                    userRoleRepository.save(userRole);
                }
            }
            return obj;
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
                userRole.setAdminUser(adminUser);
                userRole.setAdminRole(adminRole);
                userRoleRepository.save(userRole);
            }
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUser, adminUserDto);
            return adminUserDto;
        }
    }


    @Override
    public DataGridPageDto<AdminUserDto> query(Integer pageNum, Integer pageSize, String searchParams) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<AdminUser> userPage = adminUserRepository.findAll(BaseSpecs.<AdminUser>getSpecs(searchParams), pageRequest);
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
        return toDataGridPageDto(adminUserPage);
    }

    @Override
    public AdminUserDto findByLoginNameAndPassword(String loginName, String password) {
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
            return adminUserDto;
        } else
            return null;
    }
}
