package com.php25.usermicroservice.server.config;

import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.service.IdGeneratorServiceImpl;
import com.php25.common.core.service.SnowflakeIdWorker;
import com.php25.common.redis.RedisService;
import com.php25.common.redis.RedisSpringBootServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.zipkin2.ZipkinProperties;
import org.springframework.cloud.sleuth.zipkin2.ZipkinRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;
import zipkin2.reporter.Sender;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisService redisService(@Autowired StringRedisTemplate stringRedisTemplate) {
        RedisService redisService = new RedisSpringBootServiceImpl(stringRedisTemplate);
        return redisService;
    }

    @Bean
    public IdGeneratorService idGeneratorService() {
        return new IdGeneratorServiceImpl();
    }

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker(0, 0);
    }

}

