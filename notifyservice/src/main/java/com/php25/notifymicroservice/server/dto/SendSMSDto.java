package com.php25.notifymicroservice.server.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:48
 * @description:
 */
@Setter
@Getter
public class SendSMSDto {

    /**
     * 手机
     **/
    private String mobile;
}
