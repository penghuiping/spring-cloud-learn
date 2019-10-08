package com.php25.mediamicroservice.server.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author penghuiping
 * @date 2019/10/8 14:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqImageVo {

    @NotBlank
    private String content;
}
