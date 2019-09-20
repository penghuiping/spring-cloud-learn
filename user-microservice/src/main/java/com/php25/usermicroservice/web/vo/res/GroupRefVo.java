package com.php25.usermicroservice.web.vo.res;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: penghuiping
 * @date: 2019/8/12 10:59
 * @description:
 */
@Setter
@Getter
public class GroupRefVo implements Serializable {

    private Long groupId;

    private String name;
}
