package com.php25.mediamicroservice.server.config;

import com.php25.common.core.util.DigestUtil;
import com.php25.common.core.util.crypto.constant.SignAlgorithm;
import com.php25.common.core.util.crypto.key.SecretKeyUtil;
import com.php25.common.flux.web.LogFilter;
import com.php25.mediamicroservice.server.constant.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/30 18:31
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
        return http
                .authorizeExchange()
                .pathMatchers("/image/**").hasAuthority(Role.MEDIA_SERVICE_IMAGE.name())
                .pathMatchers("/actuator/**", "/static/**").permitAll()
                .anyExchange().authenticated()
                .and().oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(
                        jwt -> Mono.fromCallable(() -> {
                            Collection<String> authorities = (Collection<String>) jwt.getClaims().get("authorities");
                            log.info("authorities:{}", authorities);
                            List<SimpleGrantedAuthority> grantAuthorities = authorities.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                            return new JwtAuthenticationToken(jwt, grantAuthorities);
                        }).subscribeOn(Schedulers.elastic())
                )
                .and()
                .and().csrf().disable()
//                .and().csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
//                .and().headers().contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/")
                .build();
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
