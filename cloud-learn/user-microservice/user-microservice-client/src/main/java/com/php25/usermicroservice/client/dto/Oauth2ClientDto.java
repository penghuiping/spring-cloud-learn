package com.php25.usermicroservice.client.dto;

import com.php25.common.flux.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
@Setter
@Getter
public class Oauth2ClientDto extends BaseDto {

    private String appId;

    private String appSecret;

    private String registeredRedirectUri;
}
