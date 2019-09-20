package com.php25.h5api.config;

import com.php25.common.core.util.AssertUtil;
import com.php25.common.flux.web.LogFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/26 17:49
 * @description:
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    private static final String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKukbuhLJhDG2vrJVSEOPMViaA2dyhKmuuJh44SJvdZ5GlLJALce/pgDg2bc6r5MBxZVA6Tjx0mfwUDI9vz0ypkCAwEAAQ==";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/user/**").hasAuthority("customer")
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(new JwtAuthenticationConverter() {
                    @Override
                    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                        Collection<String> authorities = (Collection<String>) jwt.getHeaders().get("authorities");
                        log.info("authorities:{}", authorities);
                        return authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                    }
                }))
                .and().and().build();
    }


    @Order(-100)
    @Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }


    @Bean
    public NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder() {
        RSAPublicKey publicKey = (RSAPublicKey) loadPublicKey(publicKeyStr);
        return new NimbusReactiveJwtDecoder(publicKey);
    }

    private PublicKey loadPublicKey(String pubStr) {
        AssertUtil.hasText(pubStr, "pubStr不能为空");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(pubStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception var4) {
            throw new RuntimeException("出错啦!", var4);
        }
    }

}
