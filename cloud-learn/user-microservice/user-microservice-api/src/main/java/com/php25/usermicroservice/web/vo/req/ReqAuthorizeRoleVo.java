package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/21 16:36
 * @description:
 */
@Setter
@Getter
public class ReqAuthorizeRoleVo {

    private Long userId;

    private Long roleId;
}
