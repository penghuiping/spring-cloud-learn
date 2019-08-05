package com.php25.notifymicroservice.client.service.impl;

import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import com.php25.notifymicroservice.client.service.MobileMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/16 17:47
 * @description:
 */
@Slf4j
@Component
public class MobileMessageClientServiceImpl implements MobileMessageService {

    @Autowired
    @Qualifier("NotifyService_WebClient")
    private WebClient webClient;

    @Override
    public Mono<BooleanRes> sendSMS(SendSMSReq sendSMSReq) {
        return webClient
                .post()
                .uri("/mobileMsg/sendSMS")
                .syncBody(sendSMSReq)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }

    @Override
    public Mono<BooleanRes> validateSMS(Mono<ValidateSMSReq> validateSMSReqMono) {
        return webClient
                .post()
                .uri("/mobileMsg/validateSMS")
                .body(validateSMSReqMono, ValidateSMSReq.class)
                .retrieve()
                .bodyToMono(BooleanRes.class);
    }
}
