package com.php25.userservice.server.service;

import com.php25.userservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:05
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class TokenServiceTest {

    @Test
    public void generateToken() {

    }

    @Test
    public void getToken() {

    }

    @Test
    public void checkTokenValidation() {

    }

    @Test
    public void getAccessTokenExpireTime() {

    }

    @Test
    public void getRefreshTokenExpireTime() {

    }

    @Test
    public void getObjByToken() {

    }

    @Test
    public void cleanToken() {

    }
}
