package com.php25.notifymicroservice.server.config;

import com.php25.common.redis.RedisManager;
import com.php25.common.redis.RedisSpringBootManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisManager redisService(@Autowired StringRedisTemplate stringRedisTemplate) {
       return new RedisSpringBootManagerImpl(stringRedisTemplate);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

