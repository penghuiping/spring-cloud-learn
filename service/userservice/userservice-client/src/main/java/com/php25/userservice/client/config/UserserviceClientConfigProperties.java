package com.php25.userservice.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by penghuiping on 2018/3/22.
 */
@Component
@ConfigurationProperties("userservice.client")
public class UserserviceClientConfigProperties {

    private String baseUrl;


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
