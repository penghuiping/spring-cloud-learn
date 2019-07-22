package com.php25.usermicroservice.client.rpc.impl;

import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminMenuRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/15 18:25
 * @description:
 */
@Component
public class AdminMenuRpcImpl implements AdminMenuRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Flux<AdminMenuButtonBo> findAllMenuTree() {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .get()
                .uri("/adminMenu/findAllMenuTree")
                .retrieve()
                .bodyToFlux(AdminMenuButtonBo.class);
    }

    @Override
    public Mono<Boolean> hasRightAccessUrl(String url, Long adminUserId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("url", url);
        map.add("adminUserId", adminUserId.toString());

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminMenu/hasRightAccessUrl")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Flux<AdminMenuButtonBo> findAllByAdminRoleId(Long roleId) {
        MultiValueMap<String, Long> map = new LinkedMultiValueMap<>();
        map.add("roleId", roleId);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminMenu/findAllByAdminRoleId")
                .syncBody(map)
                .retrieve()
                .bodyToFlux(AdminMenuButtonBo.class);
    }
}
