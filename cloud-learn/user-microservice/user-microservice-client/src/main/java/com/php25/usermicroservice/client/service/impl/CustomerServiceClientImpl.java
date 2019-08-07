package com.php25.usermicroservice.client.service.impl;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.IdLongReq;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.ResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.ResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("Userservice_UserWebClient")
    private WebClient webClient;

    @Override
    public Mono<BooleanRes> register(CustomerDto customerDto) {
        return webClient
                .post()
                .uri("/customer/register")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByMobile(ResetPwdByMobileDto resetPwdByMobileDto) {
        return webClient
                .post()
                .uri("/customer/resetPasswordByMobile")
                .syncBody(resetPwdByMobileDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> resetPasswordByEmail(ResetPwdByEmailDto resetPwdByEmailDto) {
        return webClient
                .post()
                .uri("/customer/resetPasswordByEmail")
                .syncBody(resetPwdByEmailDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findOne(IdLongReq idLongReq) {
        return webClient
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
        return webClient
                .post()
                .uri("/customer/update")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findCustomerByMobile(StringDto mobile) {
        return webClient
                .post()
                .uri("/customer/findCustomerByMobile")
                .syncBody(mobile)
                .retrieve()
                .bodyToMono(CustomerDtoRes.class);
    }

    @Override
    public Mono<CustomerDtoRes> findCustomerByUsername(StringDto username) {
        return webClient
                .post()
                .uri("/customer/findCustomerByUsername")
                .syncBody(username)
                .retrieve()
                .bodyToMono(CustomerDtoRes.class);
    }
}
