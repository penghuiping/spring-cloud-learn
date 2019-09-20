package com.php25.notifymicroservice.server.vo.req;

import com.php25.common.validation.annotation.Mobile;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:48
 * @description:
 */
@Setter
@Getter
public class SendSMSReq {

    /**
     * 手机
     **/
    @Mobile
    private String mobile;
}
