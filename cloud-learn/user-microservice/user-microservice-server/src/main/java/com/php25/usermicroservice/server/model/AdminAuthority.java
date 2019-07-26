package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2018/10/17 13:22
 * @description: 权限表，用于后台拦截直接用户浏览器访问url
 */
@Setter
@Getter
@Table("userservice_authority")
public class AdminAuthority {
    @Id
    private Long id;

    /**
     * 权限地址
     */
    @Column
    private String url;

    /**
     * 权限名称
     */
    @Column
    private String name;

    /**
     * 权限描述
     */
    @Column
    private String description;
}
