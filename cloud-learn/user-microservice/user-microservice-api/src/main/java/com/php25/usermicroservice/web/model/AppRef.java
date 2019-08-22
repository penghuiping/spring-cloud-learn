package com.php25.usermicroservice.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:51
 * @description:
 */
@Setter
@Getter
@EqualsAndHashCode
@Table("t_user_app")
public class AppRef {

    @Column(value = "app_id")
    String appId;
}
