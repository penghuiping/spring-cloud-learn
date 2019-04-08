package com.php25.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.redis.RedisService;
import com.php25.common.redis.RedisSpringBootServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisService redisService(@Autowired StringRedisTemplate stringRedisTemplate) {
        return new RedisSpringBootServiceImpl(stringRedisTemplate);
    }

}
