package com.php25.mediamicroservice.client.rpc.impl;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.common.flux.StringRes;
import com.php25.mediamicroservice.client.bo.req.Base64ImageReq;
import com.php25.mediamicroservice.client.bo.res.ImgRes;
import com.php25.mediamicroservice.client.constant.Constant;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/17 14:45
 * @description:
 */
@Component
public class ImageRpcImpl implements ImageRpc {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<StringRes> save(Mono<Base64ImageReq> base64ImageReqMono) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/save")
                .body(base64ImageReqMono, Base64ImageReq.class)
                .retrieve()
                .bodyToMono(StringRes.class);
    }

    @Override
    public Mono<ImgRes> findOne(Mono<IdStringReq> idStringReqMono) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findOne")
                .body(idStringReqMono, IdStringReq.class)
                .retrieve()
                .bodyToMono(ImgRes.class);
    }

    @Override
    public Flux<ImgRes> findAll(Mono<IdsStringReq> idsStringReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findAll")
                .body(idsStringReqMono, IdsStringReq.class)
                .retrieve()
                .bodyToFlux(ImgRes.class);
    }
}
