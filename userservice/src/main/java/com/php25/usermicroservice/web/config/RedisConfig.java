package com.php25.usermicroservice.web.config;

import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.service.IdGeneratorServiceImpl;
import com.php25.common.redis.RedisManager;
import com.php25.common.redis.RedisSpringBootManagerImpl;
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
    public RedisManager redisManager(@Autowired StringRedisTemplate stringRedisTemplate) {
        return new RedisSpringBootManagerImpl(stringRedisTemplate);
    }

    @Bean
    public IdGeneratorService idGeneratorService() {
        return new IdGeneratorServiceImpl();
    }


}

