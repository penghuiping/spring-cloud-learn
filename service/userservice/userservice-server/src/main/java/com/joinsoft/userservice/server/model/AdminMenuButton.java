package com.joinsoft.userservice.server.model;

import com.php25.common.model.BaseSoftDeleteModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理菜单实体类
 * Created by penghuiping on 1/20/15.
 */
@Entity
@Table(name = "userservice_menu")
public class AdminMenuButton extends BaseSoftDeleteModel {

    private static final long serialVersionUID = 448179669770037196L;

    @Column(length = 45)
    private String name;

    @Column
    private String url;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<AdminMenuButton> children;

    @ManyToOne
    @JoinColumn(name = "parent")
    private AdminMenuButton parent;

    @Column(name = "is_leaf")
    private Boolean isLeaf;

    @Column(name = "sort")
    private Integer sort;//用于排序

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Column
    private String icon; //图标

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    @Column(name = "is_menu")
    private Boolean isMenu;//判断是否是菜单，不是就是按钮

    @Column
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AdminMenuButton> getChildren() {
        return children;
    }

    public void setChildren(List<AdminMenuButton> children) {
        this.children = children;
    }

    public AdminMenuButton getParent() {
        return parent;
    }

    public void setParent(AdminMenuButton parent) {
        this.parent = parent;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Boolean isMenu) {
        this.isMenu = isMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
