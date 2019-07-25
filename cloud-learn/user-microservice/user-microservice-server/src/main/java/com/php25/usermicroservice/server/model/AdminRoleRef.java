package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

/**
 * @author: penghuiping
 * @date: 2019/7/25 13:07
 * @description:
 */
@Getter
@Setter
@Table("userservice_user_role")
public class AdminRoleRef {

    @Column("role_id")
    Long roleId;
}
