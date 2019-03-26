package com.php25.notifyservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/18 16:43
 * @Description:
 */
@SpringBootApplication
@EnableTransactionManagement
public class NotifyserviceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotifyserviceApplication.class);
        app.run(args);
    }
}