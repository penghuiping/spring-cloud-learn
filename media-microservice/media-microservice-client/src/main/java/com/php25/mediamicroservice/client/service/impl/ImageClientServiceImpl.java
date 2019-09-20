package com.php25.mediamicroservice.client.service.impl;

import com.php25.common.flux.web.ReqIdString;
import com.php25.common.flux.web.ReqIdsString;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.bo.res.StringRes;
import com.php25.mediamicroservice.client.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("Mediaservice_WebClient")
    private WebClient webClient;

    @Override
    public Mono<StringRes> save(Base64ImageBo base64ImageReq) {
        return webClient
                .post()
                .uri("/img/save")
                .syncBody(base64ImageReq)
                .retrieve()
                .bodyToMono(StringRes.class);
    }

    @Override
    public Mono<ImgBoRes> findOne(ReqIdString idStringReq) {
        return webClient
                .post()
                .uri("/img/findOne")
                .syncBody(idStringReq)
                .retrieve()
                .bodyToMono(ImgBoRes.class);
    }

    @Override
    public Mono<ImgBoListRes> findAll(ReqIdsString idsStringReq) {
        return webClient
                .post()
                .uri("/img/findAll")
                .syncBody(idsStringReq)
                .retrieve()
                .bodyToMono(ImgBoListRes.class);
    }
}
