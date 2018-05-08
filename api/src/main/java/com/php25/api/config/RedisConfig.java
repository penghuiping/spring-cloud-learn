package com.php25.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.service.RedisService;
import com.php25.common.service.impl.RedisServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnExpression("'${spring.profiles.active}'.startsWith('development')")
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") String port, @Value("${spring.redis.database}") String database) {
        Config config = new Config();
        config.useSingleServer().setAddress(host + ":" + port);
        config.useSingleServer().setConnectionMinimumIdleSize(1);
        config.useSingleServer().setConnectionPoolSize(5);
        config.useSingleServer().setDatabase(Integer.parseInt(database));
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnExpression("'${spring.profiles.active}'.startsWith('test') || '${spring.profiles.active}'.startsWith('product')")
    public RedissonClient redissonClientMasterSlave(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") String port,
            @Value("${spring.redis.database}") String database,
            @Value("${spring.redis.slave-host}") String slaveHost,
            @Value("${spring.redis.slave-port}") String slavePort,
            @Value("${spring.redis.slave-database}") String slaveDatabase) {
        Config config = new Config();
        config.useMasterSlaveServers()
                .setMasterAddress(String.format("%s:%s", host, port))
                .addSlaveAddress(String.format("%s:%s", slaveHost, slavePort))
                .setDatabase(Integer.parseInt(database))
                .setReadMode(ReadMode.MASTER_SLAVE)
                .setMasterConnectionMinimumIdleSize(1)
                .setSlaveConnectionMinimumIdleSize(1)
                .setMasterConnectionPoolSize(5)
                .setSlaveConnectionPoolSize(5);

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
