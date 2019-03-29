package com.php25.gateway.config;

import com.google.common.collect.Lists;
import com.php25.gateway.filter.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


/**
 * @Auther: penghuiping
 * @Date: 2018/7/16 13:05
 * @Description:
 */
@Configuration
public class RouterConfig {

    private Logger logger = LoggerFactory.getLogger(RouterConfig.class);

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, @Autowired @Qualifier("jwtFilter") GatewayFilter jwtFilter) {
        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
        config.setParts(1);
        return builder.routes()
                .route("host_route0", r -> r.path("/a/**")
                        .filters(f -> {
                            return f.stripPrefix(1).filter(jwtFilter);
                        })
                        .uri("http://localhost:8081"))
                .build();
    }


    @Bean
    @Qualifier("jwtFilter")
    public GatewayFilter jwtFilter(@Autowired ResourceServerTokenServices resourceServerTokenServices) {
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setResourceServerTokenServices(resourceServerTokenServices);
        return jwtFilter.apply(new JwtFilter.Config(Lists.newArrayList("/api/common/SSOLogin.do", "/api/common/render.do")));
    }


}
