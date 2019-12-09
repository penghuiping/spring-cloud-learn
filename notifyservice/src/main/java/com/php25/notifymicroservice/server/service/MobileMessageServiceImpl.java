package com.php25.notifymicroservice.server.service;

import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.trace.TracedWrapper;
import com.php25.common.redis.RedisManager;
import com.php25.notifymicroservice.server.constant.Constant;
import com.php25.notifymicroservice.server.dto.SendSMSDto;
import com.php25.notifymicroservice.server.dto.ValidateSMSDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author: penghuiping
 * @date: 2018/7/19 12:56
 * @description:
 */
@Slf4j
@Service
public class MobileMessageServiceImpl implements MobileMessageService {


    @Autowired
    private RedisManager redisManager;


    @Autowired
    private TracedWrapper tracedWrapper;

    /**
     * 发送验证码
     */
    @Override
    public Mono<Boolean> sendSMS(SendSMSDto sendSMSDto) {
        return Mono.fromCallable(() -> {
            return tracedWrapper.wrap("selfDefinedSpan", () -> {
                String mobile = sendSMSDto.getMobile();
                log.info("手机号为:{}", mobile);
                String message = "1111";
                redisManager.set("sms" + mobile, message, Constant.SMS_EXPIRE_TIME);
                return true;
            });
        }).subscribeOn(Schedulers.elastic());
    }

    /**
     * 通过电话号码查询有效验证码数据
     */
    @Override
    public Mono<Boolean> validateSMS(ValidateSMSDto validateSMSDto) {
        String mobile = validateSMSDto.getMobile();
        String code = validateSMSDto.getMsgCode();

        return Mono.fromCallable(() -> {
            String mobileCode = redisManager.get("sms" + mobile, String.class);
            if (!StringUtil.isBlank(mobileCode) && mobileCode.equals(code)) {
                redisManager.remove("sms" + mobile);
                return true;
            } else {
                return false;
            }
        }).subscribeOn(Schedulers.elastic());

    }
}
