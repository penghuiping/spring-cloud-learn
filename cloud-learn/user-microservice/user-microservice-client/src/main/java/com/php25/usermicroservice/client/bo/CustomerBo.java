package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by penghuiping on 16/9/2.
 */
@Setter
@Getter
public class CustomerBo implements Serializable {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 微信
     */
    private String wx;

    /**
     * qq号
     */
    private String qq;

    /**
     * 微博号
     */
    private String weibo;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 图片id
     */
    private String imageId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    private Integer enable;

    private String jwt;
}
