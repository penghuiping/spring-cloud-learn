package com.php25.auditlogservice.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

/**
 * @author penghuiping
 * @date 2019/12/14 11:55
 */
@Configuration
public class MongoDbConfig {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.database}")
    private String dbName;


    @Bean
    public SimpleMongoClientDbFactory simpleMongoClientDbFactory() {
        String uri = String.format("mongodb://%s:%s@%s:%s/%s", username, password, host, port, dbName);
        return new SimpleMongoClientDbFactory(uri);
    }


    @Bean
    public MongoTemplate mongoTemplate(SimpleMongoClientDbFactory simpleMongoClientDbFactory) {
        return new MongoTemplate(simpleMongoClientDbFactory);
    }


}
