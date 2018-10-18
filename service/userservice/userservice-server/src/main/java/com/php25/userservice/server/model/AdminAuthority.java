package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author: penghuiping
 * @date: 2018/10/17 13:22
 * @description: 权限表，用于后台拦截直接用户浏览器访问url
 */
@Data
@Entity
@Table(name = "userservice_admin_authority")
public class AdminAuthority {
    @Id
    private Long id;

    /**
     * 权限地址
     */
    @Column
    private String url;

    /**
     * 权限名称
     */
    @Column
    private String name;

    /**
     * 权限描述
     */
    @Column
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 对应关联的菜单与按钮表id
     */
    @Column(name = "admin_menu_button_id")
    private AdminMenuButton adminMenuButton;
}
