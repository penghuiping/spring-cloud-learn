package com.php25.notifymicroservice.server.vo.req;

import com.php25.common.validation.annotation.Mobile;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:50
 * @description:
 */
@Setter
@Getter
public class ValidateSMSReq {

    /**
     * 手机号
     **/
    @Mobile
    private String mobile;

    /**
     * 验证码
     **/
    @NotBlank
    private String msgCode;
}
