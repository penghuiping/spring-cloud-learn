package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/8/21 09:53
 * @description:
 */
@Setter
@Getter
public class ReqRegisterAppVo {
    @NotBlank
    private String appId;

    @NotBlank
    private String appSecret;

    @NotBlank
    private String registeredRedirectUri;
}
