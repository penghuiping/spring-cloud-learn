package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by penghuiping on 16/3/31.
 */
@Setter
@Getter
public class AdminUserBo implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String password;

    private List<AdminRoleBo> roles;

    private List<AdminMenuButtonBo> menus;

    private Integer enable;
}
