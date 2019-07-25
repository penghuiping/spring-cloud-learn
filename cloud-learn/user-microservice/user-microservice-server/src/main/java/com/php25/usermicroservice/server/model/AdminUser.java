package com.php25.usermicroservice.server.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 后台管理用户实体类
 * Created by penghuiping on 11/30/14.
 */
@Setter
@Getter
@Table("userservice_user")
public class AdminUser {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    @Column("username")
    private String username;

    /**
     * 昵称
     */
    @Column("nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @Column("email")
    private String email;

    /**
     * 手机
     */
    @Column("mobile")
    private String mobile;

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
     * 密码
     */
    @Column
    private String password;

    /**
     * 是否有效 0:无效,1:有效,2:软删除
     */
    @Column
    private Integer enable;

    private List<AdminRoleRef> roles;



}