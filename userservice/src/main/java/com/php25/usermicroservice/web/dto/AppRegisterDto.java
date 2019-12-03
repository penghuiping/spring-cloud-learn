package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:25
 * @description:
 */
@Setter
@Getter
public class AppRegisterDto {
    private String appId;

    private String appName;

    private String appSecret;

    private String registeredRedirectUri;
}
