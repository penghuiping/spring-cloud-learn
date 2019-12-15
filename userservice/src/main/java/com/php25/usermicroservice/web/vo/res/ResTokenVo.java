package com.php25.usermicroservice.web.vo.res;

import lombok.Getter;
import lombok.Setter;

/**
 * @author penghuiping
 * @date 2019/12/14 23:46
 */
@Getter
@Setter
public class ResTokenVo {

    private String accessToken;

    private String refreshToken;

    private String expiresIn;

    private String jti;
}
