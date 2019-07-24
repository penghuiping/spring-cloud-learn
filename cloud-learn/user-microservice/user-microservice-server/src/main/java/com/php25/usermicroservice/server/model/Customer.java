package com.php25.usermicroservice.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 用户类
 * Created by penghuiping on 16/9/2.
 */
@Data
@Table("userservice_customer")
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
     * 头像
     */
    @Column(value = "image_id")
    private String imageId;

    /**
     * 创建时间
     */
    @Column(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;
}
