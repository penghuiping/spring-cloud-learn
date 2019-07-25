package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.List;

/**
 * 后台管理操作实体类
 * Created by penghuiping on 1/13/15.
 */
@Setter
@Getter
@Table("userservice_role")
public class AdminRole {

    /**
     * 主键id
     */
    @Id
    @javax.persistence.Id
    private Long id;

    /**
     * 角色代码
     */
    @Column
    private String code;

    /**
     * 角色名
     */
    @Column("role_name")
    private String name;

    /**
     * 角色描述
     */
    @Column
    private String description;

    /**
     * 创建时间
     */
    @Column("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column("update_time")
    private Date updateTime;

    /**
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;

    /**
     * 此角色对应的菜单与按钮集合
     */
    private List<AdminMenuButtonRef> adminMenuButtons;

    /**
     * 此角色对应的权限集合
     */
    private List<AdminAuthorityRef> adminAuthorities;
}
