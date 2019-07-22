package com.php25.notifymicroservice.server.controller;

import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.BooleanRes;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.rpc.MobileMessageRpc;
import com.php25.notifymicroservice.server.service.MobileMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<BooleanRes> sendSMS(@Valid Mono<SendSMSReq> sendSMSReqMono) {
        return sendSMSReqMono.map(params -> {
            String mobile = params.getMobile();
            log.info("手机号为:{}", mobile);
            var result = mobileMessageService.sendSMS(mobile);
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setReturnObject(result);
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            return booleanRes;
        });
    }

    /**
     * 通过电话号码查询有效验证码数据
     */
    @Override
    @PostMapping("/validateSMS")
    public Mono<BooleanRes> validateSMS(@Valid Mono<ValidateSMSReq> validateSMSReqMono) {
        return validateSMSReqMono.map(params -> {
            String mobile = params.getMobile();
            String code = params.getMsgCode();
            var result = mobileMessageService.validateSMS(mobile, code);
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setReturnObject(result);
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            return booleanRes;
        });
    }
}
