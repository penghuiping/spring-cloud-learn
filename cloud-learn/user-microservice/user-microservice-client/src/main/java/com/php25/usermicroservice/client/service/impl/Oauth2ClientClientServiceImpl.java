package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.web.IdStringReq;
import com.php25.common.flux.web.IdsStringReq;
import com.php25.usermicroservice.client.dto.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDtoRes;
import com.php25.usermicroservice.client.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:57
 * @description:
 */
@Component
public class Oauth2ClientClientServiceImpl implements Oauth2ClientService {

    @Autowired
    @Qualifier("Userservice_UserWebClient")
    private WebClient webClient;

    @Override
    public Mono<Oauth2ClientDtoRes> findOne(@Valid IdStringReq idStringReq) {
        return webClient
                .post()
                .uri("/oauth2/findOne")
                .header("Authorization", "Bearer " + idStringReq.getJwt())
                .syncBody(idStringReq)
                .retrieve()
                .bodyToMono(Oauth2ClientDtoRes.class);
    }

    @Override
    public Mono<BooleanRes> save(@Valid Mono<Oauth2ClientDto> oauth2ClientDtoMono) {
        return null;
    }

    @Override
    public Mono<BooleanRes> softDelete(@Valid Mono<IdsStringReq> idsStringReqMono) {
        return null;
    }
}
