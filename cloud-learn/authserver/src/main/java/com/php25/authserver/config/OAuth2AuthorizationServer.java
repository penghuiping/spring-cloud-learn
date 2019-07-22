package com.php25.authserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.php25.authserver.service.MyTokenServices;
import com.php25.authserver.service.MyUserDetailsService;
import com.php25.authserver.service.RedisAuthorizationCodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

import java.io.IOException;
import java.util.Collections;

//授权服务器配置
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
    }


    @Bean
    TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setSerializationStrategy(new JdkSerializationStrategy());
        return tokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices(redisTemplate);
        MyTokenServices myTokenServices = new MyTokenServices();
        myTokenServices.setTokenStore(tokenStore());
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(redisAuthorizationCodeServices)
                .tokenServices(myTokenServices).allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(myUserDetailsService)
                .setClientDetailsService(clientDetailsService());
    }


}
