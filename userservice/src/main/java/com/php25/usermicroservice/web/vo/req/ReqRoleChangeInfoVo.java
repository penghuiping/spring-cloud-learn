package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:47
 * @description:
 */
@Setter
@Getter
public class ReqRoleChangeInfoVo {

    @NotNull
    @Min(0)
    Long roleId;

    @NotBlank
    String description;
}
