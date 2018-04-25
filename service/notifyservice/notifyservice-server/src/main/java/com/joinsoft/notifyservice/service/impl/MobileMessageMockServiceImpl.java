package com.joinsoft.notifyservice.service.impl;

import cn.jsms.api.common.SMSClient;
import com.joinsoft.notifyservice.client.contant.Constant;
import com.joinsoft.notifyservice.service.MobileMessageService;
import com.php25.common.service.RedisService;
import com.php25.common.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Zhangbing on 2017/4/17.
 */
@Service
public class MobileMessageMockServiceImpl implements MobileMessageService {
    private Logger logger = Logger.getLogger(MobileMessageMockServiceImpl.class);

    private SMSClient client = null;

    @Autowired
    private RedisService redisService;


    @Override
    public Boolean findOneByPhoneAndCode(String mobile, String code) {
        String mobileCode = redisService.get("sms" + mobile, String.class);
        if (!StringUtil.isBlank(mobileCode) && mobileCode.equals(code)) {
            redisService.remove("sms" + mobile);
            return true;
        } else return false;
    }

    @Override
    public Boolean newMessage(String mobile) {
        String message = "1111";
        redisService.set("sms" + mobile, message, Constant.SMS_EXPIRE_TIME);
        return true;

    }
}
