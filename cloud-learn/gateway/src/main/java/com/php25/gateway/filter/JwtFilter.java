package com.php25.gateway.filter;

import com.php25.common.core.util.AssertUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.usermicroservice.client.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/12 17:59
 * @description:
 */
@Component
@Order(10)
@Slf4j
public class JwtFilter implements WebFilter {

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

    private static final String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAq6Ru6EsmEMba+slVIQ48xWJoDZ3KEqa64mHjhIm91nkaUskAtx7+mAODZtzqvkwHFlUDpOPHSZ/BQMj2/PTKmQIDAQABAkEAnkm4FfUnl5UrYNfG8AMHPChyOQxozCaCdj876IB2V5AzqlNV6lTpCh7veGz/hhCdfdA81c9Sbi6FoDlGJkfueQIhAPVt6Eb4bsZMAVO7yYIbCNFyzTfiLBIOzffExyxf51KnAiEAswj0JCXxoYaBGW7tHikNXdnkpjKkbQEBiH8WOR4U4L8CIQCdm5T8bnGEui5n/UHsYTwKdPTAnGe8uPEf2agmIPhGJQIhAIoFsRHdHrcD1qsg1TSXOXLM9HUcPZ67U89DCoLmKfpJAiBvKHPQPgw6DY5P3C/Ni5liSctCjzAZD4zedMaba7aghw==";


    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        var request = serverWebExchange.getRequest();
        var response = serverWebExchange.getResponse();
        log.info("进入JwtFilter");
        //从header中获取jwt
        String token = request.getHeaders().getFirst("Authorization");

        if (StringUtil.isBlank(token)) {

        } else {
            token = token.substring(7);
            log.info("token:{}", token);
            OAuth2Authentication oAuth2Authentication = resourceServerTokenServices.loadAuthentication(token);
            String jwt = generateJwt(oAuth2Authentication);
            request = request.mutate().header("jwt",jwt).build();
            request = request.mutate().header("username",oAuth2Authentication.getName()).build();
            log.info("生成的jwt:{}",jwt);
        }

        //认证通过
        return webFilterChain.filter(serverWebExchange.mutate().request(request).build());

    }


    public String generateJwt(OAuth2Authentication oAuth2Authentication) {
        String name = oAuth2Authentication.getName();
        List<String> list = oAuth2Authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        PrivateKey privateKey1 = loadPrivateKey(privateKey);
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.RS256, privateKey1)
                .setIssuer("www.php25.com")
                .setIssuedAt(new Date())
                .setHeaderParam("authorities", list)
                .setSubject(name)
                .compact();
        return jwt;
    }

    private PrivateKey loadPrivateKey(String priStr) {
        AssertUtil.hasText(priStr, "priStr不能为空");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(priStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception var4) {
            throw new RuntimeException("出错啦!", var4);
        }
    }
}
