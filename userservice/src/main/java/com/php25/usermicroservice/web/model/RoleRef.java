package com.php25.usermicroservice.web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
