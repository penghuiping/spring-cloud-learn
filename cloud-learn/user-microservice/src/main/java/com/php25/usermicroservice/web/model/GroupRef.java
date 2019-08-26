package com.php25.usermicroservice.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:26
 * @description:
 */
@Setter
@Getter
@EqualsAndHashCode
@Table("t_user_group")
public class GroupRef {

    @Column(value = "group_id")
    Long groupId;
}
