package com.php25.usermicroservice.web.config;

import com.php25.common.core.exception.Exceptions;
import com.php25.usermicroservice.web.service.AppClientService;
import com.php25.usermicroservice.web.service.UserService;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    @Autowired
    private AppClientService appClientService;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(appClientService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
    }

    @Bean
    TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisTemplate.getConnectionFactory());
        tokenStore.setSerializationStrategy(new JdkSerializationStrategy() {
            @Override
            protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
                return clazz.cast(OAuth2AuthorizationConfig.this.deserialize(bytes));
            }

            @Override
            protected byte[] serializeInternal(Object object) {
                return OAuth2AuthorizationConfig.this.serialize(object);
            }
        });
        return tokenStore;
    }

    private byte[] serialize(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            throw Exceptions.throwIllegalStateException("序列化对象失败", e);
        }
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            throw Exceptions.throwIllegalStateException("反序列化对象失败", e);
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices myTokenServices = new DefaultTokenServices();
        myTokenServices.setTokenStore(tokenStore());
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(appClientService)
                .tokenServices(myTokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .userDetailsService(userService)
                .pathMapping("/oauth/authorize", "/oauth2/authorize")
                .pathMapping("/oauth/token", "/oauth2/token")
                .setClientDetailsService(appClientService);
    }


}
