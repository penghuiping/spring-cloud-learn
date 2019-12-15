package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author penghuiping
 * @date 2019/12/14 23:48
 */
@Setter
@Getter
public class Oauth2TokenDto {

    private String accessToken;

    private String refreshToken;

    private String expiresIn;

    private String jti;
}
