package com.joinsoft.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.service.RedisService;
import com.php25.common.service.impl.RedisServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") String port, @Value("${spring.redis.database}") String database) {
        Config config = new Config();
        config.useSingleServer().setAddress(host + ":" + port);
        config.useSingleServer().setConnectionMinimumIdleSize(1);
        config.useSingleServer().setConnectionPoolSize(5);
        config.useSingleServer().setDatabase(Integer.parseInt(database));
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    public RedisService redisService(@Autowired RedissonClient redissonClient) {
        RedisService redisService = new RedisServiceImpl(redissonClient);
        return redisService;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
