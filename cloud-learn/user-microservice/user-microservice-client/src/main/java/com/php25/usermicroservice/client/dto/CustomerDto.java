package com.php25.usermicroservice.client.dto;

import com.php25.common.flux.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by penghuiping on 16/9/2.
 */
@Setter
@Getter
public class CustomerDto extends BaseDto implements Serializable {

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

    private Set<String> roles;

    private Integer enable;
}
