package com.php25.mediamicroservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author penghuiping
 * @date 2018/7/18 16:43
 */
@ComponentScan(value = {"com.php25.mediamicroservice", "com.php25.common.flux"})
@SpringBootApplication
@EnableTransactionManagement
public class MediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MediaServiceApplication.class);
        app.run(args);
    }
}