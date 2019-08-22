package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:51
 * @description:
 */
@Setter
@Getter
public class AppPageDto {

    private String appId;

    private String appName;

    private String appSecret;

    private String registeredRedirectUri;

    private LocalDateTime registerDate;

    private Integer enable;
}
