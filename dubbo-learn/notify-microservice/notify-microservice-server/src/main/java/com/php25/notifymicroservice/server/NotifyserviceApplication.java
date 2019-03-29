package com.php25.notifymicroservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/18 16:43
 * @Description:
 */
@ComponentScan(value = {"com.php25.notifymicroservice", "com.php25.notifyservice"})
@SpringBootApplication
@EnableTransactionManagement
public class NotifyserviceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotifyserviceApplication.class);
        app.run(args);
    }
}