package com.joinsoft.userservice.model;

import com.joinsoft.common.model.BaseSoftDeleteModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理用户实体类
 * Created by penghuiping on 11/30/14.
 */
@Entity
@Table(name = "userservice_user")
public class AdminUser extends BaseSoftDeleteModel {

    private static final long serialVersionUID = -155601402424059709L;
    @NotEmpty
    @Column(name = "login_name",length = 45)
    private String username;//用户名

    @Column(name="username",length = 50)
    private String nickname;//昵称

    @Column(name="e_mail",length = 50)
    private String email;

    @Column(name = "phone" ,length = 11)
    private String mobile;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @NotEmpty
    @Column(length = 45)
    private String password;


    @ManyToMany
    @JoinTable(
            name = "userservice_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<AdminRole> roles;

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

    public List<AdminRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AdminRole> roles) {
        this.roles = roles;
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