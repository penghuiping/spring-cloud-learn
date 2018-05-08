package com.php25.notifyservice.service;

/**
 * 短信验证码
 * Created by Zhangbing on 2017/4/17.
 */
public interface MobileMessageService {

    /**
     * 通过电话号码查询有效验证码数据
     *
     * @param mobile
     * @param code
     * @return
     */
    Boolean findOneByPhoneAndCode(String mobile, String code);

    /**
     * 发送验证短信
     *
     * @param mobile
     * @return
     */
    Boolean newMessage(String mobile);
}
