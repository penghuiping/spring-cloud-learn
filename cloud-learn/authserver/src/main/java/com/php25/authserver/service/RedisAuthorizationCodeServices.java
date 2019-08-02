package com.php25.authserver.service;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.TimeUnit;

/**
 * @author: penghuiping
 * @date: 2019/3/28 20:33
 * @description:
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    RedisTemplate redisTemplate;


    public RedisAuthorizationCodeServices(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        BoundHashOperations<String, String, byte[]> hashOperations = redisTemplate.boundHashOps("oauth2_code");
        hashOperations.put(code, SerializationUtils.serialize(authentication));
        hashOperations.getOperations().expire(code, 30L, TimeUnit.MINUTES);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        BoundHashOperations<String, String, byte[]> boundHashOperations = redisTemplate.boundHashOps("oauth2_code");
        byte[] value = boundHashOperations.get(code);
        if (null != value) {
            OAuth2Authentication auth2Authentication = SerializationUtils.deserialize(value);
            boundHashOperations.delete(code);
            return auth2Authentication;
        } else {
            return null;
        }

    }

}
