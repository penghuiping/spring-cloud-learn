package com.php25.authserver.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;

import java.util.Collections;

/**
 * @author: penghuiping
 * @date: 2019/3/27 17:06
 * @description:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public ClientDetailsService clientDetailsService() {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("clientapp");
        clientDetails.setClientSecret("112233");
        clientDetails.setRegisteredRedirectUri(Sets.newHashSet("http://localhost:9001/callback"));
        clientDetails.setAuthorizedGrantTypes(Lists.newArrayList("authorization_code"));
        clientDetails.setScope(Lists.newArrayList("read_userinfo", "read_contacts"));
        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
        clientDetailsService.setClientDetailsStore(Collections.singletonMap("clientapp", clientDetails));
        return clientDetailsService;
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new ClientDetailsUserDetailsService(clientDetailsService());
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/authorize").authenticated()
                .and().httpBasic();
//        .and().formLogin().permitAll();
    }


}
