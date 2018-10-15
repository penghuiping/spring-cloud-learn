package com.php25.userservice.server.service;

import com.php25.userservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
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

    }

    @Test
    public void getKeyByToken() {

    }

    @Test
    public void cleanToken(String token) {

    }

    @Test
    public void verifyToken() {

    }
}
