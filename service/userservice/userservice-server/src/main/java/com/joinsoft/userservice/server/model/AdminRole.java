package com.joinsoft.userservice.server.model;

import com.joinsoft.common.model.BaseSoftDeleteModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理操作实体类
 * Created by penghuiping on 1/13/15.
 */
@Entity
@Table(name = "userservice_role")
public class AdminRole extends BaseSoftDeleteModel {

    private static final long serialVersionUID = -8387195142612865703L;

    @NotEmpty
    @Column(name = "role_name",length = 50)
    private String name;

    @NotEmpty
    @Column
    private String description;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;


    @ManyToMany
    @JoinTable(
            name = "userservice_role_menu",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")}
    )
    private List<AdminMenuButton> adminMenuButtons;

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
}
