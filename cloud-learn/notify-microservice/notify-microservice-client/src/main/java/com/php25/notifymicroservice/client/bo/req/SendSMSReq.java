package com.php25.notifymicroservice.client.bo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:48
 * @description:
 */
@Setter
@Getter
public class SendSMSReq {

    /** 手机 **/
    @NotBlank
    private String mobile;
}
