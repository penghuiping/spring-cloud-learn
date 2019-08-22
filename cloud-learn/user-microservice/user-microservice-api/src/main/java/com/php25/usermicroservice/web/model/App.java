package com.php25.usermicroservice.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/4/1 16:50
 * @description:
 */
@Setter
@Getter
@EqualsAndHashCode
@Table("t_app")
public class App {

    @Id
    @Column("app_id")
    private String appId;

    @Column("app_name")
    private String appName;

    @Column("app_secret")
    private String appSecret;

    @Column("registered_redirect_uri")
    private String registeredRedirectUri;

    @CreatedDate
    @Column("register_date")
    private LocalDateTime registerDate;

    private Integer enable;

}
