package com.php25.usermicroservice.client.rpc.impl;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/15 19:40
 * @description:
 */
@Component
public class AdminUserRpcImpl implements AdminUserRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<AdminUserBo> login(Mono<LoginBo> loginBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/login")
                .body(loginBoMono, LoginBo.class)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<Boolean> resetPassword(Mono<IdsLongReq> idsLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .body(idsLongReqMono, IdsLongReq.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<Boolean> changePassword(Mono<ChangePasswordBo> changePasswordBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/changePassword")
                .body(changePasswordBoMono, ChangePasswordBo.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<AdminUserBo> findOne(Mono<IdLongReq> idLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/findOne/")
                .body(idLongReqMono, IdLongReq.class)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<AdminUserBo> save(Mono<AdminUserBo> adminUserBoMonos) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .body(adminUserBoMonos, AdminUserBo.class)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<Boolean> softDelete(Mono<IdsLongReq> idsLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/softDelete")
                .body(idsLongReqMono, IdsLongReq.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Flux<AdminUserBo> query(Mono<SearchBo> searchBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/query")
                .body(searchBoMono, SearchBo.class)
                .retrieve()
                .bodyToFlux(AdminUserBo.class);
    }
}
