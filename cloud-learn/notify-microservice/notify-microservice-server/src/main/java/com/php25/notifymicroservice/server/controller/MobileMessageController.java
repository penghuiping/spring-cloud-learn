package com.php25.notifymicroservice.server.controller;

import com.php25.common.core.exception.Exceptions;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.rpc.MobileMessageRpc;
import com.php25.notifymicroservice.server.service.MobileMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: penghuiping
 * @date: 2018/7/19 12:56
 * @description:
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/mobileMsg")
public class MobileMessageController implements MobileMessageRpc {

    @Autowired
    private MobileMessageService mobileMessageService;

    /**
     * 发送验证码
     */
    @Override
    @PostMapping("/sendSMS")
    public Mono<Boolean> sendSMS(@Valid Mono<SendSMSReq> sendSMSReqMono) {
        return sendSMSReqMono.map(params -> {
            String mobile = params.getMobile();
            log.info("手机号为:{}", mobile);
            int i = 1/0;
            return mobileMessageService.sendSMS(mobile);
        }).doOnError(throwable -> {
           log.error("出错啦!",throwable);
        });
    }

    /**
     * 通过电话号码查询有效验证码数据
     */
    @Override
    @PostMapping("/validateSMS")
    public Mono<Boolean> validateSMS(@Valid Mono<ValidateSMSReq> validateSMSReqMono) {
        return validateSMSReqMono.map(params -> {
            String mobile = params.getMobile();
            String code = params.getMsgCode();
            return mobileMessageService.validateSMS(mobile, code);
        }).doOnError(throwable -> {
            log.error("出错啦!",throwable);
        });
    }
}
