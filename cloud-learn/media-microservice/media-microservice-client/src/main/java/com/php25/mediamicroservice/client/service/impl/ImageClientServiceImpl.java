package com.php25.mediamicroservice.client.service.impl;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.constant.Constant;
import com.php25.mediamicroservice.client.service.ImageService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class ImageClientServiceImpl implements ImageService {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<String> save(Base64ImageBo base64ImageReq) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/save")
                .syncBody(base64ImageReq)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<ImgBoRes> findOne(IdStringReq idStringReq) {
        return WebClient.builder()
                .baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findOne")
                .syncBody(idStringReq)
                .retrieve()
                .bodyToMono(ImgBoRes.class);
    }

    @Override
    public Mono<ImgBoListRes> findAll(IdsStringReq idsStringReq) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/img/findAll")
                .syncBody(idsStringReq)
                .retrieve()
                .bodyToMono(ImgBoListRes.class);
    }
}
