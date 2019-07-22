package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 14:05
 * @description:
 */
@Setter
@Getter
public class LoginByMobileBo {

    /**
     * 手机号
     **/
    @NotBlank
    private String mobile;

    /**
     * 验证码
     **/
    @NotBlank
    private String code;
}
