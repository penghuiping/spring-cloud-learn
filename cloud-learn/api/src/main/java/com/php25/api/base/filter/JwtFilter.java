package com.php25.api.base.filter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.php25.common.core.util.StringUtil;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/12 17:59
 * @description:
 */
//@Component
@Slf4j
public class JwtFilter implements WebFilter {
    @Reference(check = false)
    private CustomerService customerRpc;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        var request = serverWebExchange.getRequest();
        var response = serverWebExchange.getResponse();
        try {
            String uri = request.getPath().pathWithinApplication().value();
            log.info("请求路径为:{}", uri);

            if (uri.endsWith("swagger-ui.html") || uri.endsWith("actuator")) {
                return webFilterChain.filter(serverWebExchange);
            }

            log.info("进入filter");
            //从header中获取jwt
            String token = request.getHeaders().getFirst("jwt");
            log.info("token:{}", token);

            if (StringUtil.isBlank(token)) {
                //认证失败
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            if (customerRpc.validateJwt(token)) {
                //成功
                return webFilterChain.filter(serverWebExchange);
            } else {
                //认证失败
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        } catch (Exception e) {
            log.error("jwtFilter出错!!!", e);
            //认证失败
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
    }
}
