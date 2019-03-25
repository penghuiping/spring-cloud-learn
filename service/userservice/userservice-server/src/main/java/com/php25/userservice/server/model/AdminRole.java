package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 后台管理操作实体类
 * Created by penghuiping on 1/13/15.
 */
@Data
@Entity
@Table(name = "userservice_role")
public class AdminRole {

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 角色代码
     */
    @Column
    private String code;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String name;

    /**
     * 角色描述
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
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;

    /**
     * 此角色对应的菜单与按钮集合
     */
    private List<AdminMenuButton> adminMenuButtons;

    /**
     * 此角色对应的权限集合
     */
    private List<AdminAuthority> adminAuthorities;
}
