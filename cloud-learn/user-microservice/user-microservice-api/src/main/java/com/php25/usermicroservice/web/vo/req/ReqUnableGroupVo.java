package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:35
 * @description:
 */
@Setter
@Getter
public class ReqUnableGroupVo {

    @Min(0)
    private Long groupId;
}
