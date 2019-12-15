package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author penghuiping
 * @date 2019/12/14 23:44
 */
@Setter
@Getter
public class ReqTokenVo {

    @NotBlank
    private String appId;

    @NotBlank
    private String appSecret;

    @NotBlank
    private String code;
}
