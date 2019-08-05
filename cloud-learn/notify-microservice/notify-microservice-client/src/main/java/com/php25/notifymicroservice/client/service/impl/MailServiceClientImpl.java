package com.php25.notifymicroservice.client.service.impl;

import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import com.php25.notifymicroservice.client.constant.Constant;
import com.php25.notifymicroservice.client.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("NotifyService_WebClient")
    private WebClient webClient;

    @Override
    public Mono<BooleanRes> sendSimpleMail(Mono<SendSimpleMailReq> sendSimpleMailReqMono) {
        return  webClient
                .post()
                .uri("/mail/sendSimpleMail")
                .body(sendSimpleMailReqMono, SendSimpleMailReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> sendAttachmentsMail(Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono) {
        return webClient
                .post()
                .uri("/mail/sendAttachmentsMail")
                .body(sendAttachmentsMailReqMono, SendAttachmentsMailReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }


}
