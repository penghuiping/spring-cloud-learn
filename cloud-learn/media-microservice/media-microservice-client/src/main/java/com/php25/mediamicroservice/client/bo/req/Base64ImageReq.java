package com.php25.mediamicroservice.client.bo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/19 14:13
 * @description:
 */
@Getter
@Setter
public class Base64ImageReq {

    @NotBlank
    String content;
}
