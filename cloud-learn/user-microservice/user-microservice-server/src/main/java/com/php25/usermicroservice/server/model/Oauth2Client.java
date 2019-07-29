package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author: penghuiping
 * @date: 2019/4/1 16:50
 * @description:
 */
@Setter
@Getter
@Table("userservice_oauth2_client")
public class Oauth2Client {

    @Id
    @Column("app_id")
    private String appId;

    @Column("app_secret")
    private String appSecret;

    @Column("registered_redirect_uri")
    private String registeredRedirectUri;

    private Integer enable;

}
