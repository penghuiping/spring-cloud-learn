package com.php25.usermicroservice.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;

/**
 * 后台管理操作实体类
 * Created by penghuiping on 1/13/15.
 */
@Setter
@Getter
@EqualsAndHashCode
@Table("t_role")
public class Role implements GrantedAuthority {

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 角色名
     */
    @Column("role_name")
    private String name;

    /**
     * 角色描述
     */
    @Column
    private String description;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column("create_date")
    private LocalDateTime createDate;

    @CreatedBy
    @Column("create_user_id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column("last_modified_user_id")
    private Long lastModifiedUserId;

    /**
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;

    @Column(value = "app_id")
    private String appId;

    @Override
    public String getAuthority() {
        return this.getName();
    }
}
