package com.php25.usermicroservice.client.rpc.impl;

import com.php25.common.flux.IdLongReq;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.HasRightAccessUrlBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminMenuRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
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
    public Mono<Boolean> hasRightAccessUrl(Mono<HasRightAccessUrlBo> hasRightAccessUrlBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminMenu/hasRightAccessUrl")
                .body(hasRightAccessUrlBoMono, HasRightAccessUrlBo.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Flux<AdminMenuButtonBo> findAllByAdminRoleId(Mono<IdLongReq> idLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminMenu/findAllByAdminRoleId")
                .body(idLongReqMono, IdLongReq.class)
                .retrieve()
                .bodyToFlux(AdminMenuButtonBo.class);
    }
}
