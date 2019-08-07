package com.php25.h5api.vo.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/18 16:40
 * @description:
 */
@Setter
@Getter
public class LoginByMobileReq {

    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    private String msgCode;
}
