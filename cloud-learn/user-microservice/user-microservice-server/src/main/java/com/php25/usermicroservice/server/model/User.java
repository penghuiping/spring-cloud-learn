package com.php25.usermicroservice.web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 后台管理用户实体类
 * Created by penghuiping on 11/30/14.
 */
@Setter
@Getter
@Table("userservice_user")
public class User {
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
     * 密码
     */
    @Column
    private String password;

    /**
     * 创建时间
     */
    @Column("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("update_time")
    private LocalDateTime updateTime;


    /**
     * 是否有效 0:无效,1:有效,2:软删除
     */
    @Column
    private Integer enable;

    @Column(value = "user_id")
    private Set<RoleRef> roles;

    /**
     * 头像
     */
    @Column(value = "image_id")
    private String imageId;


}