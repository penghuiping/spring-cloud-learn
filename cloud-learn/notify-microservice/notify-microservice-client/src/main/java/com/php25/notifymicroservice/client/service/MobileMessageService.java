package com.php25.notifymicroservice.client.service;

import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MobileMessageService {

    /**
     * 插入\修改一条验证码信息
     */
    Mono<BooleanRes> sendSMS(@Valid Mono<SendSMSReq> sendSMSReqMono);

    /**
     * 通过电话号码查询有效验证码数据
     */
    Mono<BooleanRes> validateSMS(@Valid Mono<ValidateSMSReq> validateSMSReqMono);
}
