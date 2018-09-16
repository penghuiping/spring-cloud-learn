package com.php25.userservice.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.DbType;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    @ConditionalOnExpression("'${spring.profiles.active}'.contains('development')")
    public DataSource druidDataSource(
            @Value("${spring.datasource.driverClassName}") String driver,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(5);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("SELECT 'x' FROM DUAL");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties = new Properties();
        properties.setProperty("druid.stat.mergeSql", "true");
        properties.setProperty("druid.stat.slowSqlMillis", "5000");
        druidDataSource.setConnectProperties(properties);
        try {
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
        }
        return druidDataSource;
    }

    @Bean
    @ConditionalOnExpression("!'${spring.profiles.active}'.contains('development')")
    public DataSource shareingJdbcDataSource(
            @Value("${spring.datasource.driverClassName}") String driver,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.slave-datasource.driverClassName}") String slaveDriver,
            @Value("${spring.slave-datasource.url}") String slaveUrl,
            @Value("${spring.slave-datasource.username}") String slaveUsername,
            @Value("${spring.slave-datasource.password}") String slavePassword

    ) {
        // 构建读写分离数据源, 读写分离数据源实现了DataSource接口, 可直接当做数据源处理. masterDataSource, slaveDataSource0, slaveDataSource1等为使用DBCP等连接池配置的真实数据源
        DruidDataSource druidDataSource_master = new DruidDataSource();
        druidDataSource_master.setDriverClassName(driver);
        druidDataSource_master.setUrl(url);
        druidDataSource_master.setUsername(username);
        druidDataSource_master.setPassword(password);
        druidDataSource_master.setInitialSize(5);
        druidDataSource_master.setMinIdle(1);
        druidDataSource_master.setMaxActive(20);
        druidDataSource_master.setMaxWait(60000);
        druidDataSource_master.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource_master.setMinEvictableIdleTimeMillis(300000);
        druidDataSource_master.setValidationQuery("SELECT 'x' FROM DUAL");
        druidDataSource_master.setTestWhileIdle(true);
        druidDataSource_master.setTestOnBorrow(false);
        druidDataSource_master.setTestOnReturn(false);
        druidDataSource_master.setPoolPreparedStatements(true);
        druidDataSource_master.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties_master = new Properties();
        properties_master.setProperty("druid.stat.mergeSql", "true");
        properties_master.setProperty("druid.stat.slowSqlMillis", "5000");
        druidDataSource_master.setConnectProperties(properties_master);
        try {
            druidDataSource_master.setFilters("stat, wall");
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
        }

        DruidDataSource druidDataSource_slave = new DruidDataSource();
        druidDataSource_slave.setDriverClassName(slaveDriver);
        druidDataSource_slave.setUrl(slaveUrl);
        druidDataSource_slave.setUsername(slaveUsername);
        druidDataSource_slave.setPassword(slavePassword);
        druidDataSource_slave.setInitialSize(5);
        druidDataSource_slave.setMinIdle(1);
        druidDataSource_slave.setMaxActive(20);
        druidDataSource_slave.setMaxWait(60000);
        druidDataSource_slave.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource_slave.setMinEvictableIdleTimeMillis(300000);
        druidDataSource_slave.setValidationQuery("SELECT 'x' FROM DUAL");
        druidDataSource_slave.setTestWhileIdle(true);
        druidDataSource_slave.setTestOnBorrow(false);
        druidDataSource_slave.setTestOnReturn(false);
        druidDataSource_slave.setPoolPreparedStatements(true);
        druidDataSource_slave.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties_slave = new Properties();
        properties_slave.setProperty("druid.stat.mergeSql", "true");
        properties_slave.setProperty("druid.stat.slowSqlMillis", "5000");
        druidDataSource_slave.setConnectProperties(properties_slave);
        try {
            druidDataSource_slave.setFilters("stat, wall");
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
        }


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
