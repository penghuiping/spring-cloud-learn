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
    APP_SECRET_NOT_VALID("00003", "appSecret不正确"),
    GROUP_ID_NOT_VALID("00004", "groupId不正确"),
    ROLE_ID_NOT_VALID("00005", "appId不正确"),
    SYSTEM_ROLE_NOT_DELETE("00006", "系统内置角色无法删除"),
    USER_ID_NOT_VALID("00007", "userId不正确"),
    NO_ACCESS("00008", "无权限访问"),
    APP_ID_ALREADY_EXISTS("00009", "appId已存在"),
    MOBILE_ALREADY_EXISTS("01000", "手机号在系统中已存在"),
    USERNAME_ALREADY_EXISTS("01001", "用户名在系统中已存在"),
    USERNAME_NOT_VALID("01002", "用户名在系统中已存在"),
    PASSWORD_NOT_VALID("01003", "密码不正确");

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
