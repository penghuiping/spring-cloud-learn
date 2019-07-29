package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.dto.AdminUserDto;
import com.php25.usermicroservice.client.dto.ChangePasswordDto;
import com.php25.usermicroservice.client.dto.LoginDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.service.AdminUserService;
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
public class UserServiceClientImpl implements AdminUserService {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<AdminUserDtoRes> login(LoginDto loginDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/login")
                .syncBody(loginDto)
                .retrieve()
                .bodyToMono(AdminUserDtoRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPassword(IdsLongReq idsLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> changePassword(ChangePasswordDto changePasswordDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/changePassword")
                .syncBody(changePasswordDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<AdminUserDtoRes> findOne(IdLongReq idLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/findOne/")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(AdminUserDtoRes.class);
    }

    @Override
    public Mono<AdminUserDtoRes> save(AdminUserDto adminUserDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(adminUserDto)
                .retrieve()
                .bodyToMono(AdminUserDtoRes.class);
    }

    @Override
    public Mono<BooleanRes> softDelete(IdsLongReq idsLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/softDelete")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<AdminUserDtoListRes> query(SearchDto searchDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/query")
                .syncBody(searchDto)
                .retrieve()
                .bodyToMono(AdminUserDtoListRes.class);
    }
}
