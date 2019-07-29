package com.php25.usermicroservice.client.dto;

import com.php25.common.flux.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 14:06
 * @description:
 */
@Getter
@Setter
public class LoginByEmailDto extends BaseDto {

    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
