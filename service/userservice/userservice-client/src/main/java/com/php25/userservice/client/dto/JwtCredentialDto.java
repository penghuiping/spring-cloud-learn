package com.php25.userservice.client.dto;

import java.io.Serializable;

/**
 * Created by penghuiping on 2018/3/15.
 */
public class JwtCredentialDto implements Serializable {

    private Long consumer_id;

    private String created_at;

    private String id;

    private String key;

    private String secret;

    private String algorithm;

    public Long getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(Long consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
