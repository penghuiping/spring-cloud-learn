package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/21 16:40
 * @description:
 */
@Setter
@Getter
public class ReqRevokeRoleVo {
    Long userId;

    Long roleId;
}
