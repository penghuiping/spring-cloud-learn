package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 14:19
 * @description:
 */
@Setter
@Getter
public class ResetPwdByMobileBo {

    @NotBlank
    private String mobile;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}