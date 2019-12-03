package com.php25.usermicroservice.web.vo.res;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
@Setter
@Getter
public class AppRefVo implements Serializable {

    private String appId;

    private String appName;
}
