package com.joinsoft.notifyservice.client.rest;

/**
 * Created by Zhangbing on 2017/4/17.
 */
public interface MobileMessageRest {

    /**
     * 插入\修改一条验证码信息
     *
     * @param mobile
     * @return
     */
    public Boolean newMessage(String mobile);

    /**
     * 通过电话号码查询有效验证码数据
     *
     * @param mobile
     * @param code
     * @return
     */
    public Boolean findOneByPhoneAndCode(String mobile, String code);
}
