package com.php25.usermicroservice.client.dto;

import com.php25.common.flux.BaseDto;
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
public class AdminUserDto extends BaseDto implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String password;

    private List<AdminRoleDto> roles;

    private List<AdminMenuButtonDto> menus;

    private Integer enable;
}
