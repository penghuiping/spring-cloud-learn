package com.php25.notifymicroservice.server.service.impl;

import com.php25.common.core.util.StringUtil;
import com.php25.common.redis.RedisService;
import com.php25.notifymicroservice.server.service.MobileMessageService;
import com.php25.notifymicroservice.server.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by Zhangbing on 2017/4/17.
 */
@Slf4j
@Service
@Primary
public class MobileMessageMockServiceImpl implements MobileMessageService {
    @Autowired
    private RedisService redisService;


    @Override
    public Boolean validateSMS(String mobile, String code) {
        String mobileCode = redisService.get("sms" + mobile, String.class);
        if (!StringUtil.isBlank(mobileCode) && mobileCode.equals(code)) {
            redisService.remove("sms" + mobile);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean sendSMS(String mobile) {
        String message = "1111";
        redisService.set("sms" + mobile, message, Constant.SMS_EXPIRE_TIME);
        return true;

    }
}
