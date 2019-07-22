package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: penghuiping
 * @date: 2019/7/22 13:49
 * @description:
 */
@Setter
@Getter
public class ChangePasswordBo {

    @NotNull
    @Min(0L)
    private Long adminUserId;

    @NotBlank
    private String originPassword;

    @NotBlank
    private String newPassword;

}
