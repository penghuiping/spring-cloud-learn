package com.php25.authserver.service;


import com.php25.common.core.util.AssertUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/28 21:02
 * @description:
 */
@Slf4j
@Service
public class JwtService {
    private static final String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAq6Ru6EsmEMba+slVIQ48xWJoDZ3KEqa64mHjhIm91nkaUskAtx7+mAODZtzqvkwHFlUDpOPHSZ/BQMj2/PTKmQIDAQABAkEAnkm4FfUnl5UrYNfG8AMHPChyOQxozCaCdj876IB2V5AzqlNV6lTpCh7veGz/hhCdfdA81c9Sbi6FoDlGJkfueQIhAPVt6Eb4bsZMAVO7yYIbCNFyzTfiLBIOzffExyxf51KnAiEAswj0JCXxoYaBGW7tHikNXdnkpjKkbQEBiH8WOR4U4L8CIQCdm5T8bnGEui5n/UHsYTwKdPTAnGe8uPEf2agmIPhGJQIhAIoFsRHdHrcD1qsg1TSXOXLM9HUcPZ67U89DCoLmKfpJAiBvKHPQPgw6DY5P3C/Ni5liSctCjzAZD4zedMaba7aghw==";


    public String generateJwt() {
        PrivateKey privateKey1 = loadPrivateKey(privateKey);
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.RS256, privateKey1)
                .setIssuer("www.php25.com")
                .setIssuedAt(new Date())
                .setHeaderParam("authorities", List.of("admin", "customer"))
                .setSubject("jack")
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

