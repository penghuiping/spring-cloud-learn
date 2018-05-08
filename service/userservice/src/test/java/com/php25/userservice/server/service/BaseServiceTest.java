package com.php25.userservice.server.service;

import com.php25.userservice.server.CommonAutoConfigure;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ContextConfiguration(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class BaseServiceTest {

    protected Logger logger = LoggerFactory.getLogger(BaseServiceTest.class);
}
