package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:48
 * @description:
 */
@Setter
@Getter
public class UserChangeDto {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String headImageId;
}
