package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:51
 * @description:
 */
@Setter
@Getter
@Table("t_user_app")
public class AppRef {

    @Column(value = "app_id")
    String appId;
}
