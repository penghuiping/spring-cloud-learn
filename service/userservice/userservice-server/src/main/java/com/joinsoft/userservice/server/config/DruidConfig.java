package com.joinsoft.userservice.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

/**
 * Created by penghuiping on 16/8/2.
 */
@Configuration
public class DruidConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
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
        try {
            druidDataSource_master.setFilters("stat, wall");
        } catch (SQLException e) {
            Logger.getLogger(DruidConfig.class).error(e);
        }

        DruidDataSource druidDataSource_slave = new DruidDataSource();
        druidDataSource_slave.setDriverClassName(slaveDriver);
        druidDataSource_slave.setUrl(slaveUrl);
        druidDataSource_slave.setUsername(slaveUsername);
        druidDataSource_slave.setPassword(slavePassword);
        try {
            druidDataSource_slave.setFilters("stat, wall");
        } catch (SQLException e) {
            Logger.getLogger(DruidConfig.class).error(e);
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
}
