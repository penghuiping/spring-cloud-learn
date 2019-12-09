package com.php25.usermicroservice.web.config;

import com.php25.usermicroservice.web.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * @author: Penghuiping
 * @Date: 2018/5/17 11:04
 * @Description:拦截器文件的配置
 */
//@EnableWebMvc
//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/appClient/**"
                        , "/group/**"
                        , "/oauth2/**"
                        , "/role/**"
                        , "/user/**")
                .excludePathPatterns("/static/**");

    }



   @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
    }


}
