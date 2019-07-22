package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/22 14:01
 * @description:
 */
@Getter
@Setter
public class StringBo {

    @NotBlank
    private String content;
}
