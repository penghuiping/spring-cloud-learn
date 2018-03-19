package com.joinsoft.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by penghuiping on 16/3/31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDto implements Serializable {
    private String id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private String password;

    private List<AdminRoleDto> roles;

    private List<AdminMenuButtonDto> menus;

    private Integer enable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<AdminRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<AdminRoleDto> roles) {
        this.roles = roles;
    }

    public List<AdminMenuButtonDto> getMenus() {
        return menus;
    }

    public void setMenus(List<AdminMenuButtonDto> menus) {
        this.menus = menus;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
