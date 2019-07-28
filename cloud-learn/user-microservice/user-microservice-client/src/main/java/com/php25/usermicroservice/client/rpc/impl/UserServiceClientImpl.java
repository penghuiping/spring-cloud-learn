package com.php25.usermicroservice.client.rpc.impl;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminUserBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminUserBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.AdminUserService;
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
    public Mono<AdminUserBoRes> login(Mono<LoginBo> loginBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/login")
                .body(loginBoMono, LoginBo.class)
                .retrieve()
                .bodyToMono(AdminUserBoRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPassword(Mono<IdsLongReq> idsLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .body(idsLongReqMono, IdsLongReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> changePassword(Mono<ChangePasswordBo> changePasswordBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/changePassword")
                .body(changePasswordBoMono, ChangePasswordBo.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<AdminUserBoRes> findOne(Mono<IdLongReq> idLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/findOne/")
                .body(idLongReqMono, IdLongReq.class)
                .retrieve()
                .bodyToMono(AdminUserBoRes.class);
    }

    @Override
    public Mono<AdminUserBoRes> save(Mono<AdminUserBo> adminUserBoMonos) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/resetPassword")
                .body(adminUserBoMonos, AdminUserBo.class)
                .retrieve()
                .bodyToMono(AdminUserBoRes.class);
    }

    @Override
    public Mono<BooleanRes> softDelete(Mono<IdsLongReq> idsLongReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/softDelete")
                .body(idsLongReqMono, IdsLongReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<AdminUserBoListRes> query(Mono<SearchBo> searchBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/adminUser/query")
                .body(searchBoMono, SearchBo.class)
                .retrieve()
                .bodyToMono(AdminUserBoListRes.class);
    }
}
