package com.php25.gateway.config;

import com.google.common.collect.Lists;
import com.php25.gateway.filter.Oauth2Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


/**
 * @Auther: penghuiping
 * @Date: 2018/7/16 13:05
 * @Description:
 */
@Slf4j
@Configuration
public class RouterConfig {


    @Bean
    @Qualifier("oauth2Filter")
    public GatewayFilter oauth2Filter(@Autowired ResourceServerTokenServices resourceServerTokenServices) {
        Oauth2Filter jwtFilter = new Oauth2Filter();
        jwtFilter.setResourceServerTokenServices(resourceServerTokenServices);
        return jwtFilter.apply(new Oauth2Filter.Config(List.of("/api/common/SSOLogin.do"
                ,"/api/common/getMsgCode.do"
                , "/api/common/render.do")));
    }


    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    CorsWebFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // Possibly...
        // config.applyPermitDefaultValues()

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
