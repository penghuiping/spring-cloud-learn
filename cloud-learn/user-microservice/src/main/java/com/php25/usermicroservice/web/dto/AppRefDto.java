package com.php25.usermicroservice.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:57
 * @description:
 */
@Setter
@Getter
@EqualsAndHashCode
public class AppRefDto {

    private String appId;

    private String appName;
}
