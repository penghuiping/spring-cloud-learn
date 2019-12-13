package com.php25.usermicroservice.web.vo.req;

import com.php25.common.validation.annotation.Email;
import com.php25.common.validation.annotation.Mobile;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: penghuiping
 * @date: 2019/8/12 11:08
 * @description:
 */
@Setter
@Getter
public class ReqChangeUserInfo implements Serializable {

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
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 头像
     */
    private String headImageId;
}
