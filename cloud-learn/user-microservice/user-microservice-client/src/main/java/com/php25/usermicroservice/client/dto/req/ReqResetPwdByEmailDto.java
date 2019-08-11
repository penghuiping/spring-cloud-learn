package com.php25.usermicroservice.client.dto.req;

import com.php25.common.flux.web.BaseDto;
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
public class ReqResetPwdByEmailDto extends BaseDto {

    @NotBlank
    private String email;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
