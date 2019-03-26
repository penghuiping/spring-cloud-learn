package com.php25.notifyservice.server.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.php25.notifyservice.client.rpc.MobileMessageRpc;
import com.php25.notifyservice.server.service.MobileMessageService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Pattern;

/**
 * Created by Zhangbing on 2017/4/17.
 */
@Service
public class MobileMessageRpcImpl implements MobileMessageRpc {

    @Autowired
    private MobileMessageService mobileMessageService;

    /**
     * 发送验证码
     *
     * @param mobile
     * @return
     */
    @Override
    public Boolean sendSMS(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机") String mobile) {
        return mobileMessageService.sendSMS(mobile);
    }

    /**
     * 通过电话号码查询有效验证码数据
     *
     * @param mobile
     * @param code
     * @return
     */
    @Override
    public Boolean validateSMS(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机") String mobile, @NotBlank String code) {
        return mobileMessageService.validateSMS(mobile, code);
    }
}
