package com.php25.usermicroservice.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:57
 * @description:
 */
@Getter
@Setter
@EqualsAndHashCode
public class GroupRefDto {

    private Long groupId;

    private String name;
}
