package com.php25.userservice.server.service;

import com.php25.common.core.util.JsonUtil;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/9/5 14:12
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class AdminMenuServiceTest {

    @Autowired
    private AdminMenuService adminMenuService;

    @Test
    public void findMenusEnabledByRole() {
        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(1L);
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findMenusEnabledByRole(adminRole);
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findMenusEnabledByParentAndRole() {
        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(1L);
        AdminMenuButtonDto parent = new AdminMenuButtonDto();
        parent.setId(1L);
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findMenusEnabledByParentAndRole(parent, adminRole);
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findRootMenus() {
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findRootMenus();
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findMenusByParent() {
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findRootMenus();
        Assert.assertTrue(listOptional.isPresent());
        listOptional = adminMenuService.findMenusByParent(listOptional.get().get(0));
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findMenusByRole() {
        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(1L);
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findMenusByRole(adminRole);
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findRootMenusEnabled() {
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findRootMenusEnabled();
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }

    @Test
    public void findMenusEnabledByParent() {
        Optional<List<AdminMenuButtonDto>> listOptional = adminMenuService.findRootMenus();
        Assert.assertTrue(listOptional.isPresent());
        listOptional = adminMenuService.findMenusEnabledByParent(listOptional.get().get(0));
        if (listOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(listOptional.get()));
        }
    }
}
