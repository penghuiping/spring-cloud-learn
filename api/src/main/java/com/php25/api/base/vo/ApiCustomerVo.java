package com.php25.api.base.vo;

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
public class ApiCustomerVo implements Serializable {
    private String id;

    private String username;

    private String password;

    private String email;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private Integer sex;//性别

    private String wx;//微信

    private String qq;//qq号

    private String weibo;//微博

    private String nickname;//昵称

    private String token;//登入tonken

    private String refreshToken;//更新token

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date tokenExpirationTime; //过期时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date refreshTokenExpirationTime;

    private String headImg;
}
