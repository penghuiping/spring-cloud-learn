package com.php25.usermicroservice.client.rpc.impl;

import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.LoginByEmailBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.bo.ResetPwdByEmailBo;
import com.php25.usermicroservice.client.bo.ResetPwdByMobileBo;
import com.php25.usermicroservice.client.bo.StringBo;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.bo.res.CustomerBoRes;
import com.php25.usermicroservice.client.bo.res.StringRes;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
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
public class CustomerRpcImpl implements CustomerRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<BooleanRes> register(Mono<CustomerBo> customerBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/register")
                .body(customerBoMono, CustomerBo.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<StringRes> loginByUsername(Mono<LoginBo> loginBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByUsername")
                .body(loginBoMono, LoginBo.class)
                .retrieve()
                .bodyToMono(StringRes.class);
    }

    @Override
    public Mono<StringRes> loginByMobile(Mono<LoginByMobileBo> loginByMobileBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByMobile")
                .body(loginByMobileBoMono, LoginByMobileBo.class)
                .retrieve()
                .bodyToMono(StringRes.class);
    }

    @Override
    public Mono<StringRes> loginByEmail(Mono<LoginByEmailBo> loginByEmailBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByEmail")
                .body(loginByEmailBoMono, LoginByEmailBo.class)
                .retrieve()
                .bodyToMono(StringRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByMobile(Mono<ResetPwdByMobileBo> resetPwdByMobileBoMono) {

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByMobile")
                .body(resetPwdByMobileBoMono, ResetPwdByMobileBo.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByEmail(Mono<ResetPwdByEmailBo> resetPwdByEmailBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByEmail")
                .body(resetPwdByEmailBoMono, ResetPwdByEmailBo.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerBoRes> findOne(Mono<IdStringReq> jwtMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findOne")
                .body(jwtMono, IdStringReq.class)
                .retrieve()
                .bodyToMono(CustomerBoRes.class);
    }

    @Override
    public Mono<BooleanRes> validateJwt(Mono<IdStringReq> jwtMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/validateJwt")
                .body(jwtMono, IdStringReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }


    @Override
    public Mono<BooleanRes> update(Mono<CustomerBo> customerBoMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/update")
                .body(customerBoMono, CustomerBo.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerBoRes> findCustomerByMobile(Mono<StringBo> mobileMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findCustomerByMobile")
                .body(mobileMono, StringBo.class)
                .retrieve()
                .bodyToMono(CustomerBoRes.class);
    }

    @Override
    public Mono<BooleanRes> logout(Mono<IdStringReq> jwtMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/logout")
                .body(jwtMono, IdStringReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<Object> testMessage() {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/testMessage")
                .retrieve()
                .bodyToMono(Object.class);
    }
}
