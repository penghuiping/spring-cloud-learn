package com.php25.h5api;


import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(value = {"com.php25", "com.php25.common.flux"})
@SpringBootApplication
@EnableTransactionManagement
@EnableApolloConfig
public class H5ApiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(H5ApiApplication.class);
        app.run(args);
    }

}