package com.joinsoft.admin;

import com.joinsoft.AdminApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by penghuiping on 12/1/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
@WebAppConfiguration
public class BaseTest {

    @Test
    public void test() {
        System.out.println("test...");
    }

}
