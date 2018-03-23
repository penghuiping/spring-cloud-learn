package com.joinsoft.userservice.client;

import com.joinsoft.userservice.client.config.UserserviceClientConfigProperties;
import com.joinsoft.userservice.client.rest.*;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by penghuiping on 2018/3/22.
 */
@ComponentScan
@Configuration
@ConditionalOnClass(Feign.class)
@ConditionalOnProperty(prefix = "userservice.client", value = "enabled", havingValue = "true")
public class UserserviceClientAutoConfigure {

    @Autowired
    private UserserviceClientConfigProperties userserviceClientConfigProperties;

    @Bean
    AdminMenuRest adminMenuRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AdminMenuRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

    @Bean
    AdminRoleRest adminRoleRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AdminRoleRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

    @Bean
    AdminUserRest adminUserRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AdminUserRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

    @Bean
    CustomerRest customerRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(CustomerRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

    @Bean
    KongJwtRest kongJwtRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(KongJwtRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

    @Bean
    TokenRest tokenRest() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TokenRest.class, userserviceClientConfigProperties.getBaseUrl());
    }

}
