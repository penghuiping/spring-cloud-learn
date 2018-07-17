package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AdminUser adminUser;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private AdminRole adminRole;
}
