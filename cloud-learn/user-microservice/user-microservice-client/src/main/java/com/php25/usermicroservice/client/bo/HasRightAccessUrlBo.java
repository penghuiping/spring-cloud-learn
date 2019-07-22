package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/7/22 13:20
 * @description:
 */
@Setter
@Getter
public class HasRightAccessUrlBo {

    private String url;

    private Long adminUserId;


}
