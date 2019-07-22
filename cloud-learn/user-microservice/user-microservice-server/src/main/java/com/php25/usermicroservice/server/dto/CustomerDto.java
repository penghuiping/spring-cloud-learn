package com.php25.usermicroservice.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by penghuiping on 16/9/2.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto implements Serializable {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;//更新时间

    private Integer enable;
}
