package com.php25.usermicroservice.client.rpc.impl;

import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminRoleRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/15 19:40
 * @description:
 */
@Component
public class AdminRoleRpcImpl implements AdminRoleRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<AdminRoleBoListRes> query(Mono<SearchBo> searchBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/query")
                .body(searchBoMono, SearchBo.class)
                .retrieve()
                .bodyToMono(AdminRoleBoListRes.class);
    }


    @Override
    public Mono<AdminRoleBoRes> save(Mono<AdminRoleBo> adminRoleBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/save")
                .body(adminRoleBoMono, AdminRoleBo.class)
                .retrieve()
                .bodyToMono(AdminRoleBoRes.class);
    }


    @Override
    public Mono<BooleanRes> softDelete(Mono<IdsLongReq> idsLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/softDelete")
                .body(idsLongReqMono, IdsLongReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }
}
