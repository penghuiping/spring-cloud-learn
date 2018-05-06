package com.php25.notifyservice.rest;

import com.php25.notifyservice.client.rest.MobileMessageRest;
import com.php25.notifyservice.service.MobileMessageService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

/**
 * Created by Zhangbing on 2017/4/17.
 */
@Validated
@RestController
@RequestMapping("/mobileMessage")
public class MobileMessageRestImpl implements MobileMessageRest {

    @Autowired
    private MobileMessageService mobileMessageService;

    /**
     * 插入\修改一条验证码信息
     *
     * @param mobile
     * @return
     */
    @RequestMapping("/newMessage")
    public Boolean newMessage(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机") @RequestParam("mobile") String mobile) {
        return mobileMessageService.newMessage(mobile);
    }

    /**
     * 通过电话号码查询有效验证码数据
     *
     * @param mobile
     * @param code
     * @return
     */
    @RequestMapping("/findOneByPhoneAndCode")
    public Boolean findOneByPhoneAndCode(@Pattern(regexp = "[0-9]{11}", message = "请输入正确手机") @RequestParam("mobile") String mobile, @NotBlank @RequestParam("code") String code) {
        return mobileMessageService.findOneByPhoneAndCode(mobile, code);
    }
}
