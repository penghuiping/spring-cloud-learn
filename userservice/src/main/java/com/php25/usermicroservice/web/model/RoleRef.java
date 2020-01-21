package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/25 13:07
 * @description:
 */
@Setter
@Getter
@Table("t_user_role")
public class RoleRef {

    @Column(value = "role_id")
    Long roleId;
}
