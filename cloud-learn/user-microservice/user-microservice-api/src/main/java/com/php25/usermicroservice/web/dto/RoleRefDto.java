package com.php25.usermicroservice.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:58
 * @description:
 */
@Getter
@Setter
@EqualsAndHashCode
public class RoleRefDto {

    private Long roleId;

    private String name;
}
