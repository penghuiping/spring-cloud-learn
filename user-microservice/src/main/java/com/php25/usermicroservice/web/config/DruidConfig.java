package com.php25.usermicroservice.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baidu.fsg.uid.UidGenerator;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.db.Db;
import com.php25.common.db.DbType;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.model.Group;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by penghuiping on 16/8/2.
 */
@Slf4j
@Configuration
@EnableJdbcRepositories(basePackages = "com.php25.usermicroservice.web.repository")
public class DruidConfig {

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private DbSlave0Properties dbSlave0Properties;

    @Bean
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
        return new Db(jdbcTemplate, DbType.POSTGRES);
    }

    @Bean
    public ApplicationListener<BeforeSaveEvent> timeStampingSaveTime(@Autowired IdGeneratorService idGeneratorService,
                                                                     @Autowired UidGenerator uidGenerator) {

        return event -> {
            Object entity = event.getEntity();
            if (entity instanceof User) {
                if (null == ((User) entity).getId()) {
                    log.info("adminUser save...");
                    User adminUser = (User) entity;
                    adminUser.setId(uidGenerator.getUID());
                }
            } else if (entity instanceof Role) {
                if (null == ((Role) entity).getId()) {
                    Role role = (Role) entity;
                    role.setId(uidGenerator.getUID());
                }
            } else if (entity instanceof App) {
                if (null == ((App) entity).getAppId()) {
                    App app = (App) entity;
                    app.setAppId(idGeneratorService.getJUID().toString());
                }
            } else if (entity instanceof Group) {
                if (null == ((Group) entity).getId()) {
                    Group group = (Group) entity;
                    group.setId(uidGenerator.getUID());
                }
            }
        };
    }
}
