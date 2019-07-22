package com.php25.usermicroservice.client.rpc.impl;

import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.constant.Constant;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public Mono<Boolean> register(CustomerBo customerDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/register")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<String> loginByUsername(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByUsername")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> loginByMobile(String mobile, String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", mobile);
        map.add("code", code);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByMobile")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> loginByEmail(String email, String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("code", code);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/loginByEmail")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<Boolean> resetPasswordByMobile(String mobile, String newPassword, String oldPassword) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", mobile);
        map.add("newPassword", newPassword);
        map.add("oldPassword", oldPassword);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByMobile")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<Boolean> resetPasswordByEmail(String email, String newPassword, String oldPassword) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", email);
        map.add("newPassword", newPassword);
        map.add("oldPassword", oldPassword);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/resetPasswordByEmail")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<CustomerBo> findOne(String jwt) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("jwt", jwt);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findOne")
                .syncBody(map)
                .retrieve()
                .bodyToMono(CustomerBo.class);
    }

    @Override
    public Mono<Boolean> validateJwt(String jwt) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("jwt", jwt);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/validateJwt")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<String> sendSms(String mobile) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", mobile);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/sendSms")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> sendEmailCode(String email) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);

        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/sendEmailCode")
                .syncBody(map)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<Boolean> update(CustomerBo customerDto) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/update")
                .syncBody(customerDto)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<CustomerBo> findCustomerByMobile(String mobile) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", mobile);
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/findCustomerByMobile")
                .syncBody(map)
                .retrieve()
                .bodyToMono(CustomerBo.class);
    }

    @Override
    public Mono<Boolean> logout(String jwt) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("jwt", jwt);
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/customer/logout")
                .syncBody(map)
                .retrieve()
                .bodyToMono(Boolean.class);
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
