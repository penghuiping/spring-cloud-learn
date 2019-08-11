package com.php25.authserver.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.ResAppDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * @author: penghuiping
 * @date: 2019/3/28 17:23
 * @description:
 */
@Slf4j
@Service
public class MyClientDetailsService implements ClientDetailsService {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Message message = rabbitMessagingTemplate.sendAndReceive("cloud-exchange", "userservice.oauth2ClientFindOne", MessageBuilder.withPayload(clientId).build());
        ResAppDto oauth2ClientDtoRes = JsonUtil.fromJson(message.getPayload().toString(), ResAppDto.class);
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
