package com.php25.gateway.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.php25.common.core.service.SnowflakeIdWorker;
import com.php25.gateway.filter.JwtFilter;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
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


/**
 * @Auther: penghuiping
 * @Date: 2018/7/16 13:05
 * @Description:
 */
@Configuration
public class RouterConfig {

    private Logger logger = LoggerFactory.getLogger(RouterConfig.class);

    @Reference(check = false)
    private CustomerRpc tokenJwtRpc;

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
    public GatewayFilter jwtFilter() {
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setTokenJwtRpc(tokenJwtRpc);
        return jwtFilter.apply(new JwtFilter.Config(Lists.newArrayList("/api/common/SSOLogin.do", "/api/common/render.do")));
    }


    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker();
    }
}
