package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author penghuiping
 * @date 2019/12/14 23:14
 */
@Getter
@Setter
public class ReqAuthorizeVo {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String appId;
}
