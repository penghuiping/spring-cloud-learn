package com.php25.userservice.client.dto;

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

    private String username;//用户名

    private String nickname;//昵称

    private String mobile;//手机

    private Integer sex;//性别

    private String wx;//微信

    private String qq;//qq号

    private String weibo;//微博

    private String password;//密码

    private String email;//邮箱

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;//更新时间

    private Integer enable;
}
