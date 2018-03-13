package com.joinsoft;


import com.joinsoft.common.repository.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class ApiApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ApiApplication.class);
        //app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }


    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(20001);
    }
}