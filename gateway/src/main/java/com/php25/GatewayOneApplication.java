package com.php25;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


/**
 * @Auther: penghuiping
 * @Date: 2018/7/16 11:08
 * @Description:
 */
@Configuration
@SpringBootApplication
@EnableDubbo
public class GatewayOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayOneApplication.class, args);
    }
}
