package com.php25.authserver.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.dto.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDtoRes;
import com.php25.usermicroservice.client.service.Oauth2ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author: penghuiping
 * @date: 2019/3/28 17:23
 * @description:
 */
@Slf4j
@Service
public class MyClientDetailsService implements ClientDetailsService {

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @Autowired
    private JwtService jwtService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        IdStringReq idStringReq = new IdStringReq();
        idStringReq.setId(clientId);
        idStringReq.setJwt(jwtService.generateJwt());
        Mono<Oauth2ClientDtoRes> oauth2ClientDtoResMono = oauth2ClientService.findOne(idStringReq);
        Oauth2ClientDtoRes oauth2ClientDtoRes = oauth2ClientDtoResMono.blockOptional(Duration.ofSeconds(5)).orElseThrow();
        Oauth2ClientDto oauth2ClientDto = oauth2ClientDtoRes.getReturnObject();
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(oauth2ClientDto.getAppId());
        clientDetails.setClientSecret(oauth2ClientDto.getAppSecret());
        clientDetails.setRegisteredRedirectUri(Sets.newHashSet(oauth2ClientDto.getRegisteredRedirectUri()));
        clientDetails.setAuthorizedGrantTypes(Lists.newArrayList("authorization_code"));
        clientDetails.setScope(Lists.newArrayList("authentication"));
        clientDetails.setAutoApproveScopes(Lists.newArrayList("authentication"));
        return clientDetails;
    }
}
