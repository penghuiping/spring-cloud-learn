package com.php25.userservice.server.service;

import com.php25.common.core.util.JsonUtil;
import com.php25.userservice.client.dto.AdminUserDto;
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
 * @date: 2018/10/12 09:54
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class AdminUserServiceTest {

    @Autowired
    private AdminUserService adminUserService;

    @Test
    public void findByLoginNameAndPassword() {
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword("jack", "123456");
        Assert.assertTrue(adminUserDtoOptional.isPresent());
        Assert.assertEquals(adminUserDtoOptional.get().getUsername(), "jack");
        log.info(JsonUtil.toPrettyJson(adminUserDtoOptional.get()));
    }

    @Test
    public void findAll() {
        Optional<List<AdminUserDto>> adminUserDtosOptional = adminUserService.findAll();
        if (adminUserDtosOptional.isPresent()) {
            log.info(JsonUtil.toPrettyJson(adminUserDtosOptional.get()));
        }
    }
}
