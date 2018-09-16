package com.php25.userservice.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;
import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.DbType;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author penghuiping
 * @date 2016-08-02
 */
@Configuration
public class DruidConfig {
    private static Logger logger = LoggerFactory.getLogger(DruidConfig.class);

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
        druidDataSource.setMinIdle(5);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(28000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(28000);
        druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");
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
            logger.error("出错啦", e);
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
        druidDataSource_master.setMinIdle(5);
        druidDataSource_master.setMaxActive(20);
        druidDataSource_master.setMaxWait(28000);
        druidDataSource_master.setTimeBetweenEvictionRunsMillis(28000);
        druidDataSource_master.setValidationQuery("SELECT 1 FROM DUAL");
        druidDataSource_master.setTestWhileIdle(true);
        druidDataSource_master.setTestOnBorrow(false);
        druidDataSource_master.setTestOnReturn(false);
        druidDataSource_master.setPoolPreparedStatements(true);
        druidDataSource_master.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties = new Properties();
        properties.setProperty("druid.stat.mergeSql", "true");
        properties.setProperty("druid.stat.slowSqlMillis", "5000");
        druidDataSource_master.setConnectProperties(properties);
        try {
            druidDataSource_master.setFilters("stat, wall");
        } catch (SQLException e) {
            logger.error("出错啦", e);
        }

        DruidDataSource druidDataSource_slave = new DruidDataSource();
        druidDataSource_slave.setDriverClassName(slaveDriver);
        druidDataSource_slave.setUrl(slaveUrl);
        druidDataSource_slave.setUsername(slaveUsername);
        druidDataSource_slave.setPassword(slavePassword);
        druidDataSource_slave.setInitialSize(5);
        druidDataSource_slave.setMinIdle(5);
        druidDataSource_slave.setMaxActive(20);
        druidDataSource_slave.setMaxWait(28000);
        druidDataSource_slave.setTimeBetweenEvictionRunsMillis(28000);
        druidDataSource_slave.setValidationQuery("SELECT 1 FROM DUAL");
        druidDataSource_slave.setTestWhileIdle(true);
        druidDataSource_slave.setTestOnBorrow(false);
        druidDataSource_slave.setTestOnReturn(false);
        druidDataSource_slave.setPoolPreparedStatements(true);
        druidDataSource_slave.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties1 = new Properties();
        properties1.setProperty("druid.stat.mergeSql", "true");
        properties1.setProperty("druid.stat.slowSqlMillis", "5000");
        druidDataSource_slave.setConnectProperties(properties1);
        try {
            druidDataSource_slave.setFilters("stat, wall");
        } catch (SQLException e) {
            logger.error("出错啦", e);
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
