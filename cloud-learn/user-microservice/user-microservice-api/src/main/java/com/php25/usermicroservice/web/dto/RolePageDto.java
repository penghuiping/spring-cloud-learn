package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/14 14:24
 * @description:
 */
@Getter
@Setter
public class RolePageDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createDate;

    private String createUserId;

    private LocalDateTime lastModifiedDate;

    private String lastModifiedUserId;

    private Integer enable;

    private String appId;
}
