package com.php25.api.config;

import com.php25.api.base.interceptor.JwtAuthenticationInterceptor;
import com.php25.common.mvc.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by penghuiping on 16/8/2.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    @Autowired
    CommonInterceptor commonInterceptor;

    @Autowired
    JwtAuthenticationInterceptor apiAuthenticationInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:assets/");
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        //解决跨域访问
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**");
            }
        };
    }


}
