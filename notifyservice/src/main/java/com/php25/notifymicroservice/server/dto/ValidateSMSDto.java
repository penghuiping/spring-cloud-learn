package com.php25.notifymicroservice.server.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:50
 * @description:
 */
@Setter
@Getter
public class ValidateSMSDto {

    /**
     * 手机号
     **/
    private String mobile;

    /**
     * 验证码
     **/
    private String msgCode;
}
