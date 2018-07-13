package com.php25.userservice.server.model;

import javax.persistence.*;

/**
 * 用户-角色实体类
 * Created by Zhangbing on 2017/4/24.
 */
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

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public AdminRole getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRole adminRole) {
        this.adminRole = adminRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
