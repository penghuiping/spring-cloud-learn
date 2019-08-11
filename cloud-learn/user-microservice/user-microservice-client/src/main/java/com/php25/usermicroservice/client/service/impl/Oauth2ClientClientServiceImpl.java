package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.web.ReqIdString;
import com.php25.common.flux.web.ReqIdsString;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.ResAppDto;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
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
    public Mono<ResAppDto> findOne(@Valid ReqIdString idStringReq) {
        return webClient
                .post()
                .uri("/oauth2/findOne")
                .syncBody(idStringReq)
                .retrieve()
                .bodyToMono(ResAppDto.class);
    }

    @Override
    public Mono<ResBoolean> save(@Valid Mono<Oauth2ClientDto> oauth2ClientDtoMono) {
        return null;
    }

    @Override
    public Mono<ResBoolean> softDelete(@Valid Mono<ReqIdsString> idsStringReqMono) {
        return null;
    }
}
