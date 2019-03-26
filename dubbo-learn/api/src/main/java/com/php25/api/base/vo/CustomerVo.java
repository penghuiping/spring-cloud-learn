package com.php25.api.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
@Data
@ApiModel(value = "前台用户信息", description = "前台用户信息")
public class CustomerVo implements Serializable {

    @ApiModelProperty(value = "用户id", name = "id")
    private String id;

    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @ApiModelProperty(value = "手机", name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "注册时间", name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "性别", name = "sex")
    private Integer sex;

    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "登入token", name = "token")
    private String token;

    @ApiModelProperty(value = "头像url", name = "image")
    private String image;
}
