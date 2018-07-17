package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "role_id")
    private AdminRole adminRole;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private AdminMenuButton adminMenuButton;
}
