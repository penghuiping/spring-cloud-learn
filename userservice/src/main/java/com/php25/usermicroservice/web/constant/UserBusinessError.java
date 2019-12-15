package com.php25.usermicroservice.web.constant;

import com.php25.common.core.exception.BusinessErrorStatus;

/**
 * @author: penghuiping
 * @date: 2019/7/29 20:07
 * @description:
 */
public enum UserBusinessError implements BusinessErrorStatus {

    USER_NOT_FOUND("00000", "用户不存在"),
    CODE_NOT_VALID("00001", "code不正确"),
    APP_ID_NOT_VALID("00002", "appId不正确"),
    APP_SECRET_NOT_VALID("00003", "appSecret不正确");


    String code;
    String desc;


    UserBusinessError(String code, String desc) {
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
