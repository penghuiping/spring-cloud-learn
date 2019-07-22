package com.php25.gateway.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/18 16:10
 * @description:
 */
@Getter
@Setter
public class GetMsgCodeReq {

    @NotBlank
    private String mobile;
}
