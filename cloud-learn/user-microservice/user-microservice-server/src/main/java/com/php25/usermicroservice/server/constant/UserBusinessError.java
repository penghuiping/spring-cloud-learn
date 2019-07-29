package com.php25.usermicroservice.server.constant;

import com.php25.common.core.exception.BusinessErrorStatus;

/**
 * @author: penghuiping
 * @date: 2019/7/29 20:07
 * @description:
 */
public enum UserBusinessError implements BusinessErrorStatus {

    USER_NOT_FOUND("00000", "用户不存在");


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
