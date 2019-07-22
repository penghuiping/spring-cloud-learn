package com.php25.usermicroservice.client.bo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: penghuiping
 * @date: 2019/7/22 10:26
 * @description:
 */

@Setter
@Getter
public class HasRightAccessUrlReq {

    @NotBlank
    private String url;

    @NotNull
    @Min(value = 0L)
    private Long adminUserId;


}
