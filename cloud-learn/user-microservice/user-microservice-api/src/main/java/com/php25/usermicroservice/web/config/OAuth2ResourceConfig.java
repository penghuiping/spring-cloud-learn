package com.php25.usermicroservice.web.config;

import com.php25.usermicroservice.web.filter.SecurityPostProcessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author: penghuiping
 * @date: 2019/8/21 14:09
 * @description:
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/appClient/**").hasAnyAuthority("admin")
                .antMatchers("/role/**").hasAnyAuthority("admin")
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/admin/**").hasAnyAuthority("admin")
                .antMatchers("/user/**").hasAnyAuthority("customer")
                .antMatchers("/actuator/**").permitAll();
    }


    @Bean
    public FilterRegistrationBean resourceFilterRegistration() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new SecurityPostProcessFilter());
        frBean.addUrlPatterns("/*");
        frBean.setName("SecurityPostProcessFilter");
        // 保证先走框架springSecurityFilterChain过滤器链，后走自定义过滤器resourceFilter，防止退出登录时报500(order不能小于-99)
        frBean.setOrder(-99);
        return frBean;
    }


}
