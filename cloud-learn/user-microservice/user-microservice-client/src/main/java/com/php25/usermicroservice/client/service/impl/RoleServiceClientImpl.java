package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.dto.AdminRoleDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.res.AdminMenuButtonDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: penghuiping
 * @date: 2019/7/15 19:40
 * @description:
 */
@Component
public class RoleServiceClientImpl implements RoleService {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<AdminRoleDtoListRes> query(SearchDto searchDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/query")
                .syncBody(searchDto)
                .retrieve()
                .bodyToMono(AdminRoleDtoListRes.class);
    }


    @Override
    public Mono<AdminRoleDtoRes> save(AdminRoleDto adminRoleDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/save")
                .syncBody(adminRoleDto)
                .retrieve()
                .bodyToMono(AdminRoleDtoRes.class);
    }


    @Override
    public Mono<BooleanRes> softDelete(IdsLongReq idsLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/softDelete")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<AdminRoleDtoRes> findOne(@Valid IdLongReq idLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/findOne")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(AdminRoleDtoRes.class);
    }

    @Override
    public Mono<AdminMenuButtonDtoListRes> findAllMenuTree() {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .get()
                .uri("/adminRole/findAllMenuTree")
                .retrieve()
                .bodyToMono(AdminMenuButtonDtoListRes.class);
    }


    @Override
    public Mono<AdminMenuButtonDtoListRes> findAllByAdminRoleId(IdLongReq idLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminRole/findAllByAdminRoleId")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(AdminMenuButtonDtoListRes.class);
    }
}
