package com.php25.notifymicroservice.server.service;

import com.php25.notifymicroservice.server.dto.SendSMSDto;
import com.php25.notifymicroservice.server.dto.ValidateSMSDto;
import reactor.core.publisher.Mono;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MobileMessageService {

    /**
     * 插入\修改一条验证码信息
     */
    Mono<Boolean> sendSMS(SendSMSDto sendSMSDto);

    /**
     * 通过电话号码查询有效验证码数据
     */
    Mono<Boolean> validateSMS(ValidateSMSDto validateSMSDto);
}
