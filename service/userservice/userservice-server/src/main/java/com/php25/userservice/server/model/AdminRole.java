package com.php25.userservice.server.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理操作实体类
 * Created by penghuiping on 1/13/15.
 */
@Entity
@Table(name = "userservice_role")
public class AdminRole {

    private static final long serialVersionUID = -8387195142612865703L;

    @Id
    private String id;//主键id

    @Column
    private String code;//角色代码

    @Column(name = "role_name", length = 50)
    private String name;//角色名

    @Column
    private String description;//角色描述

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    @Column
    private Integer enable;//是否有效 0:无效 1:有效 2:软删除

    @ManyToMany
    @JoinTable(
            name = "userservice_role_menu",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")}
    )
    private List<AdminMenuButton> adminMenuButtons;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AdminMenuButton> getAdminMenuButtons() {
        return adminMenuButtons;
    }

    public void setAdminMenuButtons(List<AdminMenuButton> adminMenuButtons) {
        this.adminMenuButtons = adminMenuButtons;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
