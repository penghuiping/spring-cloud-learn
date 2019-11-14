package com.php25.notifymicroservice.server.config;

import com.php25.common.core.util.DigestUtil;
import com.php25.common.core.util.crypto.constant.SignAlgorithm;
import com.php25.common.core.util.crypto.key.SecretKeyUtil;
import com.php25.common.flux.web.LogFilter;
import com.php25.notifymicroservice.server.constant.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/30 21:22
 * @description:
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${jwt.publicKey}")
    private String jwtPublicKey;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/mail/**").hasAuthority(Role.NOTIFY_SERVICE_MAIL.name())
                .pathMatchers("/mobile/**").hasAuthority(Role.NOTIFY_SERVICE_MOBILE.name())
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(new JwtAuthenticationConverter() {
                    @Override
                    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                        Collection<String> authorities = (Collection<String>) jwt.getClaims().get("authorities");
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
        PublicKey publicKey = SecretKeyUtil.generatePublicKey(SignAlgorithm.SHA256withRSA.getValue(), DigestUtil.decodeBase64(jwtPublicKey));
        return new NimbusReactiveJwtDecoder((RSAPublicKey) publicKey);
    }


}
