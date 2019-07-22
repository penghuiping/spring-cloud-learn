package com.php25.usermicroservice.server.service;

import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:05
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class TokenServiceTest {

    @Autowired
    private TokenService<String> tokenService;

    @Test
    public void generateToken() {
        Map<String, String> result = tokenService.generateToken("test");
        Assert.assertNotNull(result);
        log.info("结果是:" + JsonUtil.toPrettyJson(result));
    }

    @Test
    public void getToken() {
        Map<String, String> result = tokenService.generateToken("test");
        String refreshToken = result.get("refresh_token");
        result = tokenService.getToken(refreshToken);
        Assert.assertNotNull(result);
        log.info("结果是:" + JsonUtil.toPrettyJson(result));
    }

    @Test
    public void checkTokenValidation() {
        Map<String, String> result = tokenService.generateToken("test");
        String token = result.get("access_token");
        Assert.assertTrue(tokenService.checkTokenValidation(token, String.class));
    }

    @Test
    public void getAccessTokenExpireTime() {
        log.info("access_token's expire time:" + tokenService.getAccessTokenExpireTime());
    }

    @Test
    public void getRefreshTokenExpireTime() {
        log.info("refresh_token's expire time:" + tokenService.getRefreshTokenExpireTime());
    }

    @Test
    public void getObjByToken() {
        Map<String, String> result = tokenService.generateToken("test");
        String token = result.get("access_token");
        String value = tokenService.getObjByToken(token, String.class);
        Assert.assertEquals(value, "test");
    }

    @Test
    public void cleanToken() {
        Map<String, String> result = tokenService.generateToken("test");
        String token = result.get("access_token");
        Boolean tmp = tokenService.checkTokenValidation(token, String.class);
        Assert.assertTrue(tmp);
        tokenService.cleanToken(token);
        tmp = tokenService.checkTokenValidation(token, String.class);
        Assert.assertFalse(tmp);
    }
}
