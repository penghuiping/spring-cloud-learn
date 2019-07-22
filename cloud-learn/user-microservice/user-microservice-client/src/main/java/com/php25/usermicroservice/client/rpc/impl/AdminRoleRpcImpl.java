package com.php25.usermicroservice.client.rpc.impl;

import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminRoleRpc;
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
public class AdminRoleRpcImpl implements AdminRoleRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Flux<AdminRoleBo> query(SearchBo searchBo) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/query").syncBody(searchBo)
                .retrieve()
                .bodyToFlux(AdminRoleBo.class);
    }


    @Override
    public Mono<AdminRoleBo> save(AdminRoleBo adminRoleDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/save").syncBody(adminRoleDto)
                .retrieve()
                .bodyToMono(AdminRoleBo.class);
    }


    @Override
    public Mono<Boolean> softDelete(List<Long> ids) {
        MultiValueMap<String, List<Long>> map = new LinkedMultiValueMap<>();
        map.add("ids", ids);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/softDelete").syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
