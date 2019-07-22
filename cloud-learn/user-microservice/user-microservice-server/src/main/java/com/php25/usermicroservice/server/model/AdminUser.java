package com.php25.usermicroservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 后台管理用户实体类
 * Created by penghuiping on 11/30/14.
 */
@Data
@Entity
@Table(name = "userservice_user")
public class AdminUser {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机
     */
    @Column(name = "mobile")
    private String mobile;

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
     * 密码
     */
    @Column
    private String password;

    /**
     * 是否有效 0:无效,1:有效,2:软删除
     */
    @Column
    private Integer enable;

    private List<AdminRole> roles;
}