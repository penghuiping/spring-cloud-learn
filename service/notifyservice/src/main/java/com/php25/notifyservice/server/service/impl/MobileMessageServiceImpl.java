package com.php25.notifyservice.server.service.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.php25.common.core.util.RandomUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.common.redis.RedisService;
import com.php25.notifyservice.server.service.MobileMessageService;
import com.php25.notifyservice.server.service.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhangbing on 2017/4/17.
 */
@Slf4j
@Service
@Primary
public class MobileMessageServiceImpl implements MobileMessageService {

    @Value("${jiguang.sms.APP_KEY}")
    private String APP_KEY;

    @Value("${jiguang.sms.MASTER_SECRET}")
    private String MASTER_SECRET;

    private SMSClient client = null;

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
        client = new SMSClient(MASTER_SECRET, APP_KEY);
        String message = RandomUtil.getRandomNumbers(4);
        //要条用第三方接口发送短信
        Map<String, String> map = new HashMap<>();
        map.put("code", message);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobildNumber(mobile)
                .setTempId(1)
                .setTempPara(map)
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            redisService.set("sms" + mobile, message, Constant.SMS_EXPIRE_TIME);
            return true;
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
        }
        return false;
    }
}
