package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户类
 * Created by penghuiping on 16/9/2.
 */
@Data
@Entity
@Table(name = "userservice_customer")
public class Customer {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    @Column
    private String username;

    /**
     * 昵称
     */
    @Column
    private String nickname;

    /**
     * 手机
     */
    @Column
    private String mobile;

    /**
     * 密码
     */
    @Column
    private String password;

    /**
     * 邮箱
     */
    @Column
    private String email;

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
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;
}
