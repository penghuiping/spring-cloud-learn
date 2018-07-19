package com.php25.notifyservice.client.rpc;

/**
 * Created by Zhangbing on 2017/4/17.
 */
public interface MobileMessageRpc {

    /**
     * 插入\修改一条验证码信息
     *
     * @param mobile
     * @return
     */
    Boolean sendSMS(String mobile);

    /**
     * 通过电话号码查询有效验证码数据
     *
     * @param mobile
     * @param code
     * @return
     */
    Boolean findOneByPhoneAndCode(String mobile, String code);
}
