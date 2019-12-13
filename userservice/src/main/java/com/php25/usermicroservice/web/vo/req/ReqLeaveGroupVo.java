package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author: penghuiping
 * @date: 2019/8/21 16:52
 * @description:
 */
@Setter
@Getter
public class ReqLeaveGroupVo {

    @Min(0)
    Long userId;

    @Min(0)
    Long groupId;
}
