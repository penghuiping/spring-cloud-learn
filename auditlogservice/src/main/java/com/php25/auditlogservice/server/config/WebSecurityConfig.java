package com.php25.auditlogservice.server.config;

import com.php25.common.flux.web.LogFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * @author: penghuiping
 * @date: 2019/7/30 21:22
 * @description:
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Order(-100)
    @Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }

}
