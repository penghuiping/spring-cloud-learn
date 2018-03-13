package com.joinsoft.api.config;

import com.joinsoft.common.interceptor.CommonInterceptor;
import com.joinsoft.api.base.interceptor.ApiAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


/**
 * Created by penghuiping on 16/8/2.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    @Autowired
    CommonInterceptor commonInterceptor;

    @Autowired
    ApiAuthenticationInterceptor apiAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**/*");
        registry.addInterceptor(apiAuthenticationInterceptor).addPathPatterns("/api/**");
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
