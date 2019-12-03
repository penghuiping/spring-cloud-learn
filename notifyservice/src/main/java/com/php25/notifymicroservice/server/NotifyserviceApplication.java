package com.php25.notifymicroservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/18 16:43
 * @Description:
 */
@ComponentScan(value = {"com.php25.notifymicroservice.server", "com.php25.common.flux"})
@SpringBootApplication
@EnableTransactionManagement
@EnableBinding({Processor.class})
public class NotifyserviceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotifyserviceApplication.class);
        app.run(args);
    }
}