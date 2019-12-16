package com.php25.usermicroservice.web.vo.res;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/21 09:48
 * @description:
 */
@Setter
@Getter
public class ResAppDetailVo {

    private String appId;

    private String appName;

    private String appSecret;

    private String registeredRedirectUri;
}
