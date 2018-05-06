package com.php25.userservice.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by penghuiping on 2016/2/23.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminMenuButtonDto implements Serializable {
    private Long id;

    private String name;

    private String url;

    private List<AdminMenuButtonDto> children;

    private AdminMenuButtonDto parent;

    private Long parentId;

    private Boolean isShow = false;

    private Boolean isLeaf;

    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private Integer enable;

    private Boolean isMenu;

    private String description;

    private String icon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<AdminMenuButtonDto> getChildren() {
        return children;
    }

    public void setChildren(List<AdminMenuButtonDto> children) {
        this.children = children;
    }

    public AdminMenuButtonDto getParent() {
        return parent;
    }

    public void setParent(AdminMenuButtonDto parent) {
        this.parent = parent;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminMenuButtonDto that = (AdminMenuButtonDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
