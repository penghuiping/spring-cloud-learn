package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author penghuiping
 * @date 2018/3/15.
 */
@Setter
@Getter
public class JwtCredentialBo implements Serializable {

    private Long consumer_id;

    private String created_at;

    private String id;

    private String key;

    private String secret;

    private String algorithm;
}
