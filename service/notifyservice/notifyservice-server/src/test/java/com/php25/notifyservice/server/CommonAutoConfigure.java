package com.php25.notifyservice.server;

import com.php25.common.jpa.repository.BaseRepositoryImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by penghuiping on 2018/3/21.
 */
@ComponentScan
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.php25"})
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class CommonAutoConfigure {


}
