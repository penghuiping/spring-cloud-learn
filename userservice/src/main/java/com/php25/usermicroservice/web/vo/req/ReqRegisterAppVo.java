package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

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

    @URL
    private String registeredRedirectUri;
}
