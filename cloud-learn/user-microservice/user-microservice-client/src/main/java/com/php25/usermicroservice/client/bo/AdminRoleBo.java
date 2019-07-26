package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by penghuiping on 2016/2/20.
 */
@Setter
@Getter
public class AdminRoleBo implements Serializable {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer enable;

    private List<AdminMenuButtonBo> menus;

    private List<AdminAuthorityBo> authorities;
}
