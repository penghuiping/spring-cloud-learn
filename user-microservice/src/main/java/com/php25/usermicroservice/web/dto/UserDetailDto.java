package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:43
 * @description:
 */
@Getter
@Setter
public class UserDetailDto {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private LocalDateTime createDate;

    private LocalDateTime lastModifiedDate;

    private Integer enable;

    private Set<RoleRefDto> roles;

    private Set<GroupRefDto> groups;

    private Set<AppRefDto> apps;

    private String headImageId;
}
