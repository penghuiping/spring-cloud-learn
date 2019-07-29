package com.php25.usermicroservice.client.service.impl;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.IdLongReq;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.ResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.ResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class CustomerServiceClientImpl implements CustomerService {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<BooleanRes> register(CustomerDto customerDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/register")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByMobile(ResetPwdByMobileDto resetPwdByMobileDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByMobile")
                .syncBody(resetPwdByMobileDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByEmail(ResetPwdByEmailDto resetPwdByEmailDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByEmail")
                .syncBody(resetPwdByEmailDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findOne(IdLongReq idLongReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findOne")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(CustomerDtoRes.class).doOnNext(customerBoRes -> {
                    log.info(JsonUtil.toJson(customerBoRes));
                });
    }

    @Override
    public Mono<BooleanRes> update(CustomerDto customerDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/update")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findCustomerByMobile(StringDto mobile) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findCustomerByMobile")
                .header("Authorization", "Bearer " + mobile.getJwt())
                .syncBody(mobile)
                .retrieve()
                .bodyToMono(CustomerDtoRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findCustomerByUsername(StringDto username) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findCustomerByUsername")
                .header("Authorization", "Bearer " + username.getJwt())
                .syncBody(username)
                .retrieve()
                .bodyToMono(CustomerDtoRes.class);
    }

    //    @Override
//    public Mono<Object> testMessage() {
//        return WebClient.builder().baseUrl(Constant.BASE_URL)
//                .filter(lbFunction)
//                .build()
//                .post()
//                .uri("/customer/testMessage")
//                .retrieve()
//                .bodyToMono(Object.class);
//    }
}
