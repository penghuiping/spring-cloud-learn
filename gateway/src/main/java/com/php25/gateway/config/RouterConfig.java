package com.php25.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;


/**
 * @Auther: penghuiping
 * @Date: 2018/7/16 13:05
 * @Description:
 */
@Configuration
public class RouterConfig {

    private Logger logger = LoggerFactory.getLogger(RouterConfig.class);

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
        config.setParts(1);
        return builder.routes()
                .route("host_route", r -> r.path("/a/**").filters(f -> f.stripPrefix(1)).uri("http://localhost:8081"))
                .route("host_route", r -> r.path("/b/**").filters(f -> f.stripPrefix(1)).uri("http://localhost:8082"))
                .build();
    }

    @Bean
    @Order(0)
    public GlobalFilter jwtFilter() {
        return (exchange, chain) -> {
            logger.info("second pre filter");
            ServerHttpResponse response = exchange.getResponse();
            boolean flag = true;
            if (flag) {
                //认证通过
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    logger.info("second post filter");
                }));
            } else {
                //认证失败
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

        };
    }


}
