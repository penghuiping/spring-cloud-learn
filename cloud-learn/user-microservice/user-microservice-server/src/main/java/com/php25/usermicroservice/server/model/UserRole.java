package com.php25.usermicroservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户-角色实体类
 * Created by Zhangbing on 2017/4/24.
 */
@Data
@Entity
@Table(name = "userservice_user_role")
public class UserRole {

    @Id
    private Long id;

    @Column(name = "user_id")
    private AdminUser adminUser;

    @Column(name = "role_id")
    private AdminRole adminRole;
}
