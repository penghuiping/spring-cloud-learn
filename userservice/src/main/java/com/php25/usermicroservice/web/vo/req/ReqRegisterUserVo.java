package com.php25.usermicroservice.web.vo.req;

import com.php25.common.validation.annotation.Email;
import com.php25.common.validation.annotation.Mobile;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by penghuiping on 16/9/2.
 */
@Setter
@Getter
public class ReqRegisterUserVo implements Serializable {

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    @Mobile
    private String mobile;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 邮箱
     */
    @Email
    private String email;

    @NotBlank
    private String appId;

}
