package com.php25.userservice.server.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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
    @Id
    private Long id;//主键id

    @NotEmpty
    @Column(name = "username", length = 45)
    private String username;//用户名

    @Column(name = "nickname", length = 50)
    private String nickname;//昵称

    @Column(name = "email", length = 50)
    private String email;//邮箱

    @Column(name = "mobile", length = 11)
    private String mobile;//手机

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    @NotEmpty
    @Column(length = 45)
    private String password;//密码

    @Column
    private Integer enable;//是否有效 0:无效,1:有效,2:软删除


    @ManyToMany
    @JoinTable(
            name = "userservice_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<AdminRole> roles;
}