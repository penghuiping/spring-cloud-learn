package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

/**
 * @author: penghuiping
 * @date: 2019/7/25 13:15
 * @description:
 */
@Setter
@Getter
@Table("userservice_role_menu")
public class AdminMenuButtonRef {

    @Column("menu_id")
    private Long menuId;


}
