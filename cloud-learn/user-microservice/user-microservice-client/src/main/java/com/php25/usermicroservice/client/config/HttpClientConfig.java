package com.php25.usermicroservice.client.config;

import com.php25.usermicroservice.client.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author: penghuiping
 * @date: 2019/8/5 10:33
 * @description:
 */
@Configuration("Userservice_HttpClientConfig")
public class HttpClientConfig {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Bean(name = "Userservice_UserWebClient")
    WebClient webClient() {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build();

    }
}
