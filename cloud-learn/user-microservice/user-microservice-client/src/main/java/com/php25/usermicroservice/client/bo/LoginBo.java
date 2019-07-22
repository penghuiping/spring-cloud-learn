package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 13:52
 * @description:
 */
@Setter
@Getter
public class LoginBo {

    @NotBlank
    String username;

    @NotBlank
    String password;
}
