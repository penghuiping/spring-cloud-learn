package com.php25.notifyservice.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.service.RedisService;
import com.php25.common.service.impl.RedisSpringBootServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
@ConditionalOnExpression("'${server.type}'.contains('provider')")
public class RedisConfig {

    @Bean
    public RedisService redisService(@Autowired StringRedisTemplate stringRedisTemplate) {
        RedisService redisService = new RedisSpringBootServiceImpl(stringRedisTemplate);
        return redisService;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

