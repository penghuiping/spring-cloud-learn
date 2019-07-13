package com.php25.gateway.config;

import com.google.common.collect.Lists;
import com.php25.gateway.filter.Oauth2Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


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
        return jwtFilter.apply(new Oauth2Filter.Config(Lists.newArrayList("/api/common/SSOLogin.do", "/api/common/render.do")));
    }


}
