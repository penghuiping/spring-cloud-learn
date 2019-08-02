package com.php25.usermicroservice.server;


import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author penghuiping
 * @date
 */
@ComponentScan(value = {"com.php25.usermicroservice", "com.php25.common.flux"})
@SpringBootApplication
@EnableTransactionManagement
@EnableApolloConfig
@EnableBinding({Processor.class})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.run(args);
    }
}