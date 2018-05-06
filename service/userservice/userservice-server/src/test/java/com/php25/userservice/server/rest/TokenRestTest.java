package com.php25.userservice.server.rest;

import com.php25.userservice.client.rest.TokenRest;
import com.php25.userservice.server.CommonAutoConfigure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ContextConfiguration(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class TokenRestTest {

    @Autowired
    TokenRest tokenRest;

    @Test
    public void getTokenByObjId() {
        System.out.println(tokenRest.getTokenByObjId("123123123"));
    }
}
