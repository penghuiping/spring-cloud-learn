package com.joinsoft;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
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