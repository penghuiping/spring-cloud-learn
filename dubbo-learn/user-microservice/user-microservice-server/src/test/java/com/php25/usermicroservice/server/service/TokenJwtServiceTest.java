package com.php25.usermicroservice.server.service;

import com.php25.usermicroservice.server.CommonAutoConfigure;
import com.php25.userservice.server.service.TokenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class TokenJwtServiceTest {

    @Autowired
    private TokenJwtService tokenJwtService;

    @Test
    public void getToken() {
        String token = tokenJwtService.getToken("test");
        Assert.assertNotNull(token);
        log.info("token:" + token);
    }

    @Test
    public void getKeyByToken() {
        String token = tokenJwtService.getToken("test");
        String key = tokenJwtService.getKeyByToken(token);
        Assert.assertEquals(key, "test");
    }

    @Test
    public void cleanToken() {
        String token = tokenJwtService.getToken("test");
        Boolean result = tokenJwtService.cleanToken(token);
        Assert.assertTrue(result);
    }

    @Test
    public void verifyToken() {
        String token = tokenJwtService.getToken("test");
        Boolean result = tokenJwtService.verifyToken(token);
        Assert.assertTrue(result);
        tokenJwtService.cleanToken(token);
        result = tokenJwtService.verifyToken(token);
        Assert.assertFalse(result);
    }
}
