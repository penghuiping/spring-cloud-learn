package com.php25.usermicroservice.client.dto.req;

import com.php25.common.flux.web.BaseDto;
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
public class ReqLoginDto extends BaseDto {

    @NotBlank
    String username;

    @NotBlank
    String password;
}
