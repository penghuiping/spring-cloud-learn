package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/14 14:43
 * @description:
 */
@Getter
@Setter
public class GroupCreateDto {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createDate;

    private Long createUserId;

    private LocalDateTime lastModifiedDate;

    private Long lastModifiedUserId;

    private Long appId;

    private Integer enable;
}
