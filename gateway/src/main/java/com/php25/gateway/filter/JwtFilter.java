package com.php25.gateway.filter;

import com.php25.userservice.client.rpc.TokenJwtRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 14:23
 * @Description:
 */
@Slf4j
@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private TokenJwtRpc tokenJwtRpc;

    public void setTokenJwtRpc(TokenJwtRpc tokenJwtRpc) {
        this.tokenJwtRpc = tokenJwtRpc;
    }

    @Override
    public GatewayFilter apply(Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            log.info("second pre filter");
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpRequest request = exchange.getRequest();
            //首先判断是否需要拦截
            for (String excludeUri : config.excludeUris) {
                log.info(request.getURI().toString());
                if (request.getURI().toString().contains(excludeUri)) {
                    //如果是不需要拦截的直接跳过
                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        log.info("second post filter");
                    }));
                }
            }

            //从http header中获取jwt
            String jwt = request.getHeaders().getFirst("jwt");

            if (!StringUtils.isEmpty(jwt)) {
                if (tokenJwtRpc.verifyToken(jwt)) {
                    //认证通过
                    String customerId = tokenJwtRpc.getKeyByToken(jwt);
                    request = request.mutate().header("customerId", customerId).build();
                    return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
                        log.info("second post filter");
                    }));
                }
            }

            //从params中获取
            MultiValueMap<String, String> multiValueMap = request.getQueryParams();
            jwt = multiValueMap.getFirst("jwt");

            if (!StringUtils.isEmpty(jwt)) {
                if (tokenJwtRpc.verifyToken(jwt)) {
                    //认证通过
                    String customerId = tokenJwtRpc.getKeyByToken(jwt);
                    request = request.mutate().header("customerId", customerId).build();
                    return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
                        log.info("second post filter");
                    }));
                }
            }

            //认证失败
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }


    public static class Config {

        List<String> excludeUris;


        public Config(List<String> excludeUris) {
            if (null == excludeUris) {
                this.excludeUris = new ArrayList<>();
            } else
                this.excludeUris = excludeUris;
        }
    }
}
