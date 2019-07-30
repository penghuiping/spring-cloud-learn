package com.php25.notifymicroservice.client.service.impl;

import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.constant.Constant;
import com.php25.notifymicroservice.client.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/16 17:46
 * @description:
 */
@Slf4j
@Component
public class MailServiceClientImpl implements MailService {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Override
    public Mono<Boolean> sendSimpleMail(Mono<SendSimpleMailReq> sendSimpleMailReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/mail/sendSimpleMail")
                .body(sendSimpleMailReqMono, SendSimpleMailReq.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<Boolean> sendAttachmentsMail(Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono) {
        return WebClient.builder().baseUrl(Constant.BASE_URL)
                .filter(lbFunction)
                .build()
                .post()
                .uri("/mail/sendAttachmentsMail")
                .body(sendAttachmentsMailReqMono, SendAttachmentsMailReq.class)
                .retrieve()
                .bodyToMono(Boolean.class);
    }


}
