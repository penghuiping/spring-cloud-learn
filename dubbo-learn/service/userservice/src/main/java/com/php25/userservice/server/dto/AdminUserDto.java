package com.php25.userservice.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by penghuiping on 16/3/31.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDto implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private String password;

    private List<AdminRoleDto> roles;

    private List<AdminMenuButtonDto> menus;

    private Integer enable;
}
