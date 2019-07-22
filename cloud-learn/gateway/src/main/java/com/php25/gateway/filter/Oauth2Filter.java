package com.php25.gateway.filter;

import com.php25.common.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 14:23
 * @Description:
 */
@Slf4j
public class Oauth2Filter extends AbstractGatewayFilterFactory<Oauth2Filter.Config> {

    private ResourceServerTokenServices resourceServerTokenServices;

    public ResourceServerTokenServices getResourceServerTokenServices() {
        return resourceServerTokenServices;
    }

    public void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }

    @Override
    public GatewayFilter apply(Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            log.info("enter into Oauth2 filter");
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpRequest request = exchange.getRequest();
            //首先判断是否需要拦截
            for (String excludeUri : config.excludeUris) {
                log.info(request.getURI().toString());
                if (request.getURI().toString().contains(excludeUri)) {
                    //如果是不需要拦截的直接跳过
                    return chain.filter(exchange);
                }
            }

            //从http header中获取token
            String token = request.getHeaders().getFirst("Authorization").substring(6).trim();
            log.info("token:{}", token);

            if (!StringUtil.isEmpty(token)) {
                //验证token合法性
                OAuth2Authentication oAuth2Authentication = resourceServerTokenServices.loadAuthentication(token);

                if (null == oAuth2Authentication) {
                    //认证失败
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                if (!oAuth2Authentication.getUserAuthentication().isAuthenticated()) {
                    //认证失败
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                //认证通过
                return chain.filter(exchange.mutate().request(request).build());
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
            } else {
                this.excludeUris = excludeUris;
            }
        }
    }
}
