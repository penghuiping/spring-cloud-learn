package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 13:49
 * @description:
 */
@Setter
@Getter
public class ReqChangePasswordVo {

    @NotBlank
    private String originPassword;

    @NotBlank
    private String newPassword;

}
