package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色-菜单中间关系表
 * Created by Zhangbing on 2017/4/24.
 */
@Data
@Entity
@Table(name = "userservice_role_menu")
public class RoleMenu {

    @Id
    private Long id;

    @Column(name = "role_id")
    private AdminRole adminRole;

    @Column(name = "menu_id")
    private AdminMenuButton adminMenuButton;
}
