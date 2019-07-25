package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author: penghuiping
 * @date: 2019/7/25 13:20
 * @description:
 */
@Getter
@Setter
@Table("userservice_admin_role_authority")
public class AdminAuthorityRef {

    @Column("authority_id")
    private Long authorityId;
}
