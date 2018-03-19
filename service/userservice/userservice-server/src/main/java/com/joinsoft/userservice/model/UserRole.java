package com.joinsoft.userservice.model;

import com.joinsoft.common.model.BaseModel;

import javax.persistence.*;

/**
 * 用户-角色实体类
 * Created by Zhangbing on 2017/4/24.
 */
@Entity
@Table(name = "userservice_user_role")
public class UserRole extends BaseModel {

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
}
