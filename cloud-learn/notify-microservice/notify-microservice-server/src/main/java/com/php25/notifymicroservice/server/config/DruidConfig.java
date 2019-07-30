package com.php25.notifymicroservice.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.php25.common.db.Db;
import com.php25.common.db.DbType;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
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
@ConditionalOnExpression("'${server.type}'.contains('provider')")
public class DruidConfig {

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private DbSlave0Properties dbSlave0Properties;

    @Bean
    @ConditionalOnExpression("'${spring.profiles.active}'.contains('development')")
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
            //druidDataSource.setFilters("stat, wall");
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
    @ConditionalOnExpression("!'${spring.profiles.active}'.contains('development')")
    public DataSource shareingJdbcDataSource() {
        // 构建读写分离数据源, 读写分离数据源实现了DataSource接口, 可直接当做数据源处理. masterDataSource, slaveDataSource0, slaveDataSource1等为使用DBCP等连接池配置的真实数据源
        DruidDataSource druidDataSource_master = new DruidDataSource();
        druidDataSource_master.setDriverClassName(dbProperties.getDriverClassName());
        druidDataSource_master.setUrl(dbProperties.getUrl());
        druidDataSource_master.setUsername(dbProperties.getUsername());
        druidDataSource_master.setPassword(dbProperties.getPassword());
        druidDataSource_master.setInitialSize(dbProperties.getInitSize());
        druidDataSource_master.setMinIdle(dbProperties.getMinIdle());
        druidDataSource_master.setMaxActive(dbProperties.getMaxActive());
        druidDataSource_master.setMaxWait(dbProperties.getMaxWait());
        druidDataSource_master.setTimeBetweenEvictionRunsMillis(dbProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource_master.setMinEvictableIdleTimeMillis(dbProperties.getMinEvictableIdleTimeMillis());
        druidDataSource_master.setValidationQuery(dbProperties.getValidationQuery());
        druidDataSource_master.setTestWhileIdle(dbProperties.getTestWhileIdle());
        druidDataSource_master.setTestOnBorrow(dbProperties.getTestOnBorrow());
        druidDataSource_master.setTestOnReturn(dbProperties.getTestOnReturn());
        druidDataSource_master.setPoolPreparedStatements(dbProperties.getPoolPreparedStatements());
        druidDataSource_master.setMaxPoolPreparedStatementPerConnectionSize(dbProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            //druidDataSource_master.setFilters("stat, wall");
            druidDataSource_master.setFilters("config");
        } catch (SQLException e) {
            log.error("druid设置过滤器失败", e);
        }
        Properties properties_master = new Properties();
        //properties_master.setProperty("druid.stat.mergeSql", "true");
        //properties_master.setProperty("druid.stat.slowSqlMillis", "5000");
        properties_master.setProperty("config.decrypt", dbProperties.getDecrypt());
        properties_master.setProperty("config.decrypt.key", dbProperties.getPublicKey());
        druidDataSource_master.setConnectProperties(properties_master);


        DruidDataSource druidDataSource_slave = new DruidDataSource();
        druidDataSource_slave.setDriverClassName(dbSlave0Properties.getDriverClassName());
        druidDataSource_slave.setUrl(dbSlave0Properties.getUrl());
        druidDataSource_slave.setUsername(dbSlave0Properties.getUsername());
        druidDataSource_slave.setPassword(dbSlave0Properties.getPassword());
        druidDataSource_slave.setInitialSize(dbSlave0Properties.getInitSize());
        druidDataSource_slave.setMinIdle(dbSlave0Properties.getMinIdle());
        druidDataSource_slave.setMaxActive(dbSlave0Properties.getMaxActive());
        druidDataSource_slave.setMaxWait(dbSlave0Properties.getMaxWait());
        druidDataSource_slave.setTimeBetweenEvictionRunsMillis(dbSlave0Properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource_slave.setMinEvictableIdleTimeMillis(dbSlave0Properties.getMinEvictableIdleTimeMillis());
        druidDataSource_slave.setValidationQuery(dbSlave0Properties.getValidationQuery());
        druidDataSource_slave.setTestWhileIdle(dbSlave0Properties.getTestWhileIdle());
        druidDataSource_slave.setTestOnBorrow(dbSlave0Properties.getTestOnBorrow());
        druidDataSource_slave.setTestOnReturn(dbSlave0Properties.getTestOnReturn());
        druidDataSource_slave.setPoolPreparedStatements(dbSlave0Properties.getPoolPreparedStatements());
        druidDataSource_slave.setMaxPoolPreparedStatementPerConnectionSize(dbSlave0Properties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            //druidDataSource_slave.setFilters("stat, wall");
            druidDataSource_master.setFilters("config");
        } catch (SQLException e) {
            log.error("druid设置过滤器失败", e);
        }
        Properties properties_slave = new Properties();
//        properties_slave.setProperty("druid.stat.mergeSql", "true");
//        properties_slave.setProperty("druid.stat.slowSqlMillis", "5000");
        properties_slave.setProperty("config.decrypt", dbSlave0Properties.getDecrypt());
        properties_slave.setProperty("config.decrypt.key", dbSlave0Properties.getPublicKey());
        druidDataSource_slave.setConnectProperties(properties_slave);

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("masterDataSource", druidDataSource_master);
        dataSourceMap.put("slaveDataSource0", druidDataSource_slave);

        // 构建读写分离配置
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig.setName("ms_ds");
        masterSlaveRuleConfig.setMasterDataSourceName("masterDataSource");
        masterSlaveRuleConfig.getSlaveDataSourceNames().add("slaveDataSource0");

        DataSource dataSource = null;
        try {
            dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, Maps.newHashMap());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
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
    public Db db(JdbcTemplate jdbcTemplate) {
        return new Db(jdbcTemplate, DbType.MYSQL);
    }
}
