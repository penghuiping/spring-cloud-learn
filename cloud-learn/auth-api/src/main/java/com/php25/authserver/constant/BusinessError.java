package com.php25.authserver.constant;

import com.php25.common.core.exception.BusinessErrorStatus;

/**
 * @author: penghuiping
 * @date: 2019/1/3 18:34
 * @description:
 */
public enum BusinessError implements BusinessErrorStatus {
    MOBILE_CODE_ERROR("21001", "短信验证码不正确"),
    MOBILE_ALREADY_EXIST_ERROR("21003", "此手机号系统已存在");

    public String code;

    public String desc;

    BusinessError(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
