package com.php25.usermicroservice.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.php25.common.db.Db;
import com.php25.common.db.DbType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by penghuiping on 16/8/2.
 */
@Slf4j
@Configuration
public class DruidConfig {

    @Autowired
    private DbProperties dbProperties;

    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dbProperties.getDriverClassName());
        druidDataSource.setUrl(dbProperties.getUrl());
        druidDataSource.setUsername(dbProperties.getUsername());
        druidDataSource.setPassword(dbProperties.getPassword());
        druidDataSource.setInitialSize(dbProperties.getInitSize());
        druidDataSource.setMinIdle(dbProperties.getMinIdle());
        druidDataSource.setMaxActive(dbProperties.getMaxActive());
        druidDataSource.setMaxWait(dbProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(dbProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(dbProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(dbProperties.getValidationQuery());
        druidDataSource.setTestWhileIdle(dbProperties.getTestWhileIdle());
        druidDataSource.setTestOnBorrow(dbProperties.getTestOnBorrow());
        druidDataSource.setTestOnReturn(dbProperties.getTestOnReturn());
        druidDataSource.setPoolPreparedStatements(dbProperties.getPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dbProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
//            druidDataSource.setFilters("stat, wall");
            druidDataSource.setFilters("config");
        } catch (SQLException e) {
            log.error("druid设置过滤器失败", e);
        }

        Properties properties = new Properties();
        //properties.setProperty("druid.stat.mergeSql", "true");
        //properties.setProperty("druid.stat.slowSqlMillis", "5000");
        properties.setProperty("config.decrypt", dbProperties.getDecrypt());
        properties.setProperty("config.decrypt.key", dbProperties.getPublicKey());
        druidDataSource.setConnectProperties(properties);
        return druidDataSource;
    }


    @Bean
    public DataSource shardDatasource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(new TableRuleConfiguration("t_user", "ds0.t_user${0..2}"));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "t_user${id % 3}"));
        Map<String, DataSource> result = new HashMap<>();
        result.put("ds0", druidDataSource());
        return ShardingDataSourceFactory.createDataSource(result, shardingRuleConfig, new Properties());
    }


    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public Db db(JdbcTemplate jdbcTemplate) {
        Db db =  new Db(DbType.POSTGRES);
        db.setJdbcOperations(jdbcTemplate);
        db.scanPackage("com.php25.usermicroservice.web.model");
        return db;
    }

}
