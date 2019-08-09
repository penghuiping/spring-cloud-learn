package com.php25.authserver.config;

import com.php25.authserver.service.MyClientDetailsService;
import com.php25.authserver.service.MyUserDetailsService;
import com.php25.authserver.service.RedisAuthorizationCodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

//授权服务器配置
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {


    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Autowired
    private MyClientDetailsService clientDetailsService;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
    }

    @Bean
    TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisTemplate.getConnectionFactory());
        tokenStore.setSerializationStrategy(new JdkSerializationStrategy());
        return tokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices(redisTemplate);
        DefaultTokenServices myTokenServices = new DefaultTokenServices();
        myTokenServices.setTokenStore(tokenStore());
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(redisAuthorizationCodeServices)
                .tokenServices(myTokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(myUserDetailsService)
                .setClientDetailsService(clientDetailsService);
    }


}
