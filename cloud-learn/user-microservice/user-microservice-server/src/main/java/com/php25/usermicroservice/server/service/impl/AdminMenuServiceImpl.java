package com.php25.usermicroservice.server.service.impl;

import com.php25.common.db.service.BaseServiceImpl;
import com.php25.usermicroservice.server.dto.AdminMenuButtonDto;
import com.php25.usermicroservice.server.dto.AdminRoleDto;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.repository.AdminMenuButtonRepository;
import com.php25.usermicroservice.server.service.AdminMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author penghuiping
 * @date 2019-07-11
 */
@Slf4j
@Service
@Primary
@Transactional(rollbackFor = Exception.class)
public class AdminMenuServiceImpl implements AdminMenuService {
    private AdminMenuButtonRepository adminMenuButtonRepository;

    private BaseServiceImpl<AdminMenuButtonDto, AdminMenuButton, Long> baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        baseService = new BaseServiceImpl<>(AdminMenuButtonDto.class, AdminMenuButton.class, adminMenuButtonRepository);
    }

    @Autowired
    public void setAdminMenuButtonRepository(AdminMenuButtonRepository adminMenuButtonRepository) {
        this.adminMenuButtonRepository = adminMenuButtonRepository;
    }

    @Override
    public Optional<AdminMenuButtonDto> findOne(Long id) {
        var adminMenuButton = adminMenuButtonRepository.findById(id).orElse(null);
        if (adminMenuButton == null) {
            return Optional.empty();
        }
        Long parentId = null;
        if (null != adminMenuButton.getParent()) {
            adminMenuButton.getParent().getId();
        }
        var adminMenuButtonDto = new AdminMenuButtonDto();
        BeanUtils.copyProperties(adminMenuButton, adminMenuButtonDto, "children", "parent");
        adminMenuButtonDto.setParentId(parentId);
        return Optional.of(adminMenuButtonDto);
    }

    @Override
    public Optional<AdminMenuButtonDto> save(AdminMenuButtonDto menu) {
        AdminMenuButton result = null;
        if (null != menu.getId() && menu.getId() > 0) {
            //编辑
            var adminMenuButton = new AdminMenuButton();
            BeanUtils.copyProperties(menu, adminMenuButton);

            if (null != menu.getParent()) {
                var parent = new AdminMenuButton();
                BeanUtils.copyProperties(menu.getParent(), parent);
                adminMenuButton.setParent(parent);
            }
            adminMenuButton.setUpdateTime(LocalDateTime.now());
            result = adminMenuButtonRepository.save(adminMenuButton);
        } else {
            //新增
            var sort = adminMenuButtonRepository.findMenusMaxSort();
            menu.setSort(sort + 1);
            if (null == menu.getParentId() || menu.getParentId() == 0) {
                menu.setIsLeaf(false);
                var adminMenuButton = new AdminMenuButton();
                BeanUtils.copyProperties(menu, adminMenuButton);
                adminMenuButton.setCreateTime(LocalDateTime.now());
                adminMenuButton.setUpdateTime(LocalDateTime.now());
                result = adminMenuButtonRepository.save(adminMenuButton);
            } else {
                var parent = adminMenuButtonRepository.findById(menu.getParentId()).orElse(null);
                if (null == parent) {
                    return Optional.empty();
                }
                adminMenuButtonRepository.save(parent);
                var adminMenuButton = new AdminMenuButton();
                BeanUtils.copyProperties(menu, adminMenuButton);
                adminMenuButton.setParent(parent);
                adminMenuButton.setCreateTime(LocalDateTime.now());
                adminMenuButton.setUpdateTime(LocalDateTime.now());
                result = adminMenuButtonRepository.save(adminMenuButton);
            }
        }
        var adminMenuButtonDto = new AdminMenuButtonDto();
        BeanUtils.copyProperties(result, adminMenuButtonDto, "parent", "children");
        return Optional.of(adminMenuButtonDto);
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findMenusEnabledByParentAndRole(AdminMenuButtonDto parent, AdminRoleDto adminRoleDto) {
        var adminMenuButton_ = new AdminMenuButton();
        BeanUtils.copyProperties(parent, adminMenuButton_);
        var adminRole = new AdminRole();
        BeanUtils.copyProperties(adminRoleDto, adminRole);
        var adminMenuButtons = adminMenuButtonRepository.findMenusEnabledByParentAndRole(adminMenuButton_, adminRole);
        return Optional.of(adminMenuButtons.stream().map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findMenusEnabledByRole(AdminRoleDto adminRoleDto) {
        var adminRole = new AdminRole();
        BeanUtils.copyProperties(adminRoleDto, adminRole);
        var adminMenuButtons = adminMenuButtonRepository.findMenusEnabledByRole(adminRole);
        return Optional.of(adminMenuButtons.stream()
                .map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findRootMenus() {
        var adminMenuButtons = adminMenuButtonRepository.findRootMenus();
        return Optional.of(adminMenuButtons.stream()
                .filter(adminMenuButton -> adminMenuButton.getEnable() != 2)
                .map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findMenusByParent(AdminMenuButtonDto parent) {
        var adminMenuButton_ = new AdminMenuButton();
        BeanUtils.copyProperties(parent, adminMenuButton_);
        var adminMenuButtons = adminMenuButtonRepository.findMenusByParent(adminMenuButton_);
        return Optional.of(adminMenuButtons.stream()
                .map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findMenusByRole(AdminRoleDto role) {
        var adminRole = new AdminRole();
        BeanUtils.copyProperties(role, adminRole);
        var adminMenuButtons = adminMenuButtonRepository.findMenusByRole(adminRole);
        return Optional.of(adminMenuButtons.stream()
                .map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findRootMenusEnabled() {
        var adminMenuButtons = adminMenuButtonRepository.findRootMenusEnabled();
        return Optional.ofNullable(adminMenuButtons.stream().map(adminMenuButton -> {
            var adminMenuButtonDto = new AdminMenuButtonDto();
            BeanUtils.copyProperties(adminMenuButton, adminMenuButtonDto, "children");
            return adminMenuButtonDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminMenuButtonDto>> findMenusEnabledByParent(AdminMenuButtonDto parent) {
        var adminMenuButton_ = new AdminMenuButton();
        BeanUtils.copyProperties(parent, adminMenuButton_);
        var adminMenuButtons = adminMenuButtonRepository.findMenusEnabledByParent(adminMenuButton_);
        return Optional.of(adminMenuButtons.stream()
                .map(this::trans).collect(Collectors.toList()));
    }

    //此方法用于转换对象
    private AdminMenuButtonDto trans(AdminMenuButton adminMenuButton) {
        Long parentId = null;
        if (null != adminMenuButton.getParent()) {
            parentId = adminMenuButton.getParent().getId();
        }
        var adminMenuButtonBo = new AdminMenuButtonDto();
        BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo, "children", "parent");
        adminMenuButtonBo.setParentId(parentId);
        return adminMenuButtonBo;
    }


    @Override
    public void softDelete(AdminMenuButtonDto obj) {
        baseService.softDelete(obj);
        if (!obj.getIsLeaf()) {
            var adminMenuButton = new AdminMenuButton();
            adminMenuButton.setId(obj.getId());
            var adminMenuButtons = adminMenuButtonRepository.findMenusEnabledByParent(adminMenuButton);
            var adminMenuButtonDtos = adminMenuButtons.stream().map(adminMenuButton1 -> {
                AdminMenuButtonDto adminMenuButtonDto = new AdminMenuButtonDto();
                BeanUtils.copyProperties(adminMenuButton1, adminMenuButtonDto);
                return adminMenuButtonDto;
            }).collect(Collectors.toList());
            softDelete(adminMenuButtonDtos);
        }
    }

    @Override
    public void softDelete(List<AdminMenuButtonDto> objs) {
        for (AdminMenuButtonDto obj : objs) {
            softDelete(obj);
        }
    }
}
