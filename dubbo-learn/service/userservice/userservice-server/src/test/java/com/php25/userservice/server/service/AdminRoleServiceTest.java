package com.php25.userservice.server.service;

import com.php25.common.core.util.JsonUtil;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class AdminRoleServiceTest {

    @Autowired
    private AdminRoleService adminRoleService;

    @Test
    public void findAllEnabled() {
        Optional<List<AdminRoleDto>> adminRoleDtosOptional = adminRoleService.findAllEnabled();
        if (adminRoleDtosOptional.isPresent()) {
            log.info(JsonUtil.toPrettyJson(adminRoleDtosOptional.get()));
        }
    }

    @Test
    public void findAll() {
        Optional<List<AdminRoleDto>> adminRoleDtosOptional = adminRoleService.findAll();
        if (adminRoleDtosOptional.isPresent()) {
            log.info(JsonUtil.toPrettyJson(adminRoleDtosOptional.get()));
        }
    }
}
