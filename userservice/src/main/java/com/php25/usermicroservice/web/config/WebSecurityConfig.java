package com.php25.usermicroservice.web.config;

import com.php25.common.core.util.DigestUtil;
import com.php25.common.core.util.crypto.constant.SignAlgorithm;
import com.php25.common.core.util.crypto.key.SecretKeyUtil;
import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.filter.SecurityPostProcessFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2019/3/27 17:06
 * @description:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.privateKey}")
    private String jwtPrivateKey;

    @Value("${jwt.publicKey}")
    private String jwtPublicKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/authorize","/oauth2/token","/user/register", "/static/**","/actuator/**").permitAll()
                .antMatchers("/appClient/**").hasAnyAuthority("SCOPE_" +Constants.Role.SUPER_ADMIN)
                .antMatchers("/role/**").hasAnyAuthority("SCOPE_" +Constants.Role.ADMIN)
                .antMatchers("/user/admin/**").hasAnyAuthority("SCOPE_" +Constants.Role.ADMIN)
                .antMatchers("/user/**").hasAnyAuthority("SCOPE_" +Constants.Role.CUSTOMER)
                .and().addFilterAfter(new SecurityPostProcessFilter(), AnonymousAuthenticationFilter.class)
                .oauth2ResourceServer().jwt()
                .decoder(jwtDecoder()).jwtAuthenticationConverter(new JwtAuthenticationConverter())
                .and()
                .and().csrf().disable();
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//          .and().headers().contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
    }

    public JwtDecoder jwtDecoder() {
        String publicKeyBase64 = jwtPublicKey;
        RSAPublicKey publicKey = (RSAPublicKey) SecretKeyUtil.generatePublicKey(SignAlgorithm.SHA256withRSA.getValue(), DigestUtil.decodeBase64(publicKeyBase64));
        return s -> {
            JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
            Jws<Claims> jwt = jwtParser.parseClaimsJws(s);

            Instant issueAt = jwt.getBody().getIssuedAt().toInstant();
            Instant expiration = jwt.getBody().getExpiration().toInstant();

            Map<String, Object> headers = new HashMap<>();
            if (null != jwt.getHeader() && !jwt.getHeader().isEmpty()) {
                jwt.getHeader().forEach((Object key, Object value) -> {
                    headers.put((String) key, value);
                });
            }

            Map<String, Object> bodys = new HashMap<>();
            if (null != jwt.getBody() && !jwt.getBody().isEmpty()) {
                jwt.getBody().forEach((Object key, Object value) -> {
                    bodys.put((String) key, value);
                });
            }
            bodys.put("scp", headers.get("scp"));
            return new Jwt(s, issueAt, expiration, headers, bodys);
        };

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
