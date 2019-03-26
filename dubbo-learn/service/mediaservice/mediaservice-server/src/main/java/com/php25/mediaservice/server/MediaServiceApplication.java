package com.php25.mediaservice.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author penghuiping
 * @date 2018/7/18 16:43
 */
@SpringBootApplication
@EnableTransactionManagement
public class MediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MediaServiceApplication.class);
        app.run(args);
    }
}