package com.php25.api.base.constant;

import com.php25.common.mvc.ReturnStatus;

/**
 * @author: penghuiping
 * @date: 2019/1/3 18:34
 * @description:
 */
public enum BusinessError implements ReturnStatus {
    COMMON_ERROR(10000, "出错啦,请重试"),
    KAPTCHA_ERROR(20000, "图形验证码不正确"),
    MOBILE_CODE_ERROR(20001, "短信验证码不正确"),
    MOBILE_NOT_EXIST_ERROR(20002, "手机号系统不存在"),
    MOBILE_ALREADY_EXIST_ERROR(20003, "此手机号系统已存在"),
    USERNAME_PASSWORD_ERROR(20004, "用户名与密码不匹配");

    public int value;

    public String desc;

    BusinessError(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
