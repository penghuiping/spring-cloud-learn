package com.php25.usermicroservice.client.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Data
public class JwtCredentialBo implements Serializable {

    private Long consumer_id;

    private String created_at;

    private String id;

    private String key;

    private String secret;

    private String algorithm;
}
