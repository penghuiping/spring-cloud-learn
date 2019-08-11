package com.php25.usermicroservice.client.service.impl;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ReqIdLong;
import com.php25.usermicroservice.client.dto.req.ReqResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.req.ReqResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.req.ReqStringDto;
import com.php25.usermicroservice.client.dto.res.CustomerDto;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.dto.res.ResCustomerDto;
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
    public Mono<ResBoolean> register(CustomerDto customerDto) {
        return webClient
                .post()
                .uri("/customer/register")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResBoolean> resetPasswordByMobile(ReqResetPwdByMobileDto resetPwdByMobileDto) {
        return webClient
                .post()
                .uri("/customer/resetPasswordByMobile")
                .syncBody(resetPwdByMobileDto)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResBoolean> resetPasswordByEmail(ReqResetPwdByEmailDto resetPwdByEmailDto) {
        return webClient
                .post()
                .uri("/customer/resetPasswordByEmail")
                .syncBody(resetPwdByEmailDto)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResCustomerDto> findOne(ReqIdLong idLongReq) {
        return webClient
                .post()
                .uri("/customer/findOne")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(ResCustomerDto.class).doOnNext(customerBoRes -> {
                    log.info(JsonUtil.toJson(customerBoRes));
                });
    }

    @Override
    public Mono<ResBoolean> update(CustomerDto customerDto) {
        return webClient
                .post()
                .uri("/customer/update")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResCustomerDto> findCustomerByMobile(ReqStringDto mobile) {
        return webClient
                .post()
                .uri("/customer/findCustomerByMobile")
                .syncBody(mobile)
                .retrieve()
                .bodyToMono(ResCustomerDto.class);
    }

    @Override
    public Mono<ResCustomerDto> findCustomerByUsername(ReqStringDto username) {
        return webClient
                .post()
                .uri("/customer/findCustomerByUsername")
                .syncBody(username)
                .retrieve()
                .bodyToMono(ResCustomerDto.class);
    }
}
