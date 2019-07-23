package com.php25.mediamicroservice.client.rpc.impl;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.constant.Constant;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
    public Mono<String> save(Mono<Base64ImageBo> base64ImageReqMono) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/save")
                .body(base64ImageReqMono, Base64ImageBo.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<ImgBoRes> findOne(Mono<IdStringReq> idStringReqMono) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findOne")
                .body(idStringReqMono, IdStringReq.class)
                .retrieve()
                .bodyToMono(ImgBoRes.class);
    }

    @Override
    public Mono<ImgBoListRes> findAll(Mono<IdsStringReq> idsStringReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findAll")
                .body(idsStringReqMono, IdsStringReq.class)
                .retrieve()
                .bodyToMono(ImgBoListRes.class);
    }
}
