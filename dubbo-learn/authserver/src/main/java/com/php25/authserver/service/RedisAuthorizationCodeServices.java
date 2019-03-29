package com.php25.authserver.service;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

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
        redisTemplate.boundHashOps("oauth2_code").put(code, authentication);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        BoundHashOperations<String, String, OAuth2Authentication> boundHashOperations = redisTemplate.boundHashOps("oauth2_code");
        OAuth2Authentication oAuth2Authentication = boundHashOperations.get(code);
        boundHashOperations.delete(code);
        return oAuth2Authentication;
    }
}
