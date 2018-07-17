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
}
