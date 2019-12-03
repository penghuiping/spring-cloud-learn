package com.php25.usermicroservice.web.vo.res;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by penghuiping on 2016/2/20.
 */
@Setter
@Getter
public class RoleRefVo implements Serializable {

    private Long roleId;

    private String name;

}
