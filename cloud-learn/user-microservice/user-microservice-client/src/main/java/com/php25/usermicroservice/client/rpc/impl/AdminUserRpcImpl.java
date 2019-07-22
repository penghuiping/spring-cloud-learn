package com.php25.usermicroservice.client.rpc.impl;

import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<AdminUserBo> login(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/login")
                .syncBody(map)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<Boolean> resetPassword(List<Long> adminUserIds) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(adminUserIds)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<Boolean> changePassword(Long adminUserId, String originPassword, String newPassword) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("adminUserId", adminUserId.toString());
        map.add("originPassword", originPassword);
        map.add("newPassword", newPassword);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/changePassword")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<AdminUserBo> findOne(Long id) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .get()
                .uri("/adminUser/findOne/{id}", id)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<AdminUserBo> save(AdminUserBo adminUserDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(adminUserDto)
                .retrieve()
                .bodyToMono(AdminUserBo.class);
    }

    @Override
    public Mono<Boolean> softDelete(List<Long> ids) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/softDelete")
                .syncBody(ids)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Flux<AdminUserBo> query(SearchBo searchBo) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/query")
                .syncBody(searchBo)
                .retrieve()
                .bodyToFlux(AdminUserBo.class);
    }
}
