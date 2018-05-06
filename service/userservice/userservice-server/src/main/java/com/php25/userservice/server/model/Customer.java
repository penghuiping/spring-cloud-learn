package com.php25.userservice.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户类
 * Created by penghuiping on 16/9/2.
 */
@Entity
@Table(name = "userservice_customer")
public class Customer {
    @Id
    private Long id;//主键id

    @Column
    private Integer enable;//是否有效 0:无效 1:有效 2:软删除

    @Column
    private String username;//用户名

    @Column
    private String nickname;//昵称

    @Column
    private String mobile;//手机

    @Column
    private Integer sex;//性别

    @Column
    private String wx;//微信

    @Column
    private String qq;//qq号

    @Column
    private String weibo;//微博

    @Column
    private String password;//密码

    @Column(name = "email")
    private String email;//邮箱

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
