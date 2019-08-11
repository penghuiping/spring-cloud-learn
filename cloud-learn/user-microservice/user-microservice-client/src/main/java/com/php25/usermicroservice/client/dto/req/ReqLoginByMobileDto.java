package com.php25.usermicroservice.client.dto.req;

import com.php25.common.flux.web.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 14:05
 * @description:
 */
@Setter
@Getter
public class ReqLoginByMobileDto extends BaseDto {

    /**
     * 手机号
     **/
    @NotBlank
    private String mobile;

    /**
     * 验证码
     **/
    @NotBlank
    private String code;
}
