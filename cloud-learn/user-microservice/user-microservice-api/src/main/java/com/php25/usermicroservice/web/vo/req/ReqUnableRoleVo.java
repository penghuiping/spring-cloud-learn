package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:35
 * @description:
 */
@Setter
@Getter
public class ReqUnableRoleVo {

    @Min(0)
    private Long roleId;
}
