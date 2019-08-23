package com.php25.usermicroservice.web.config;

import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.filter.SecurityPostProcessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

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
                .antMatchers("/appClient/**").hasAnyAuthority(Constants.Role.SUPER_ADMIN)
                .antMatchers("/role/**").hasAnyAuthority(Constants.Role.ADMIN)
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/admin/**").hasAnyAuthority(Constants.Role.ADMIN)
                .antMatchers("/user/**").hasAnyAuthority(Constants.Role.CUSTOMER)
                .antMatchers("/actuator/**").permitAll().and().addFilterAfter(new SecurityPostProcessFilter(), AnonymousAuthenticationFilter.class);
    }
}
