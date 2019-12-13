package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author: penghuiping
 * @date: 2019/8/21 16:36
 * @description:
 */
@Setter
@Getter
public class ReqAuthorizeRoleVo {

    @Min(0)
    private Long userId;

    @Min(0)
    private Long roleId;
}
