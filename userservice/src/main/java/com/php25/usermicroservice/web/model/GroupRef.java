package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:26
 * @description:
 */
@Setter
@Getter
@Table("t_user_group")
public class GroupRef {

    @Column(value = "group_id")
    Long groupId;
}
