package com.php25.usermicroservice.server;

import com.php25.common.core.util.AssertUtil;
import com.php25.common.core.util.DigestUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2019/7/27 22:31
 * @description:
 */
@Slf4j
public class JwtTest {

    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKukbuhLJhDG2vrJVSEOPMViaA2dyhKmuuJh44SJvdZ5GlLJALce/pgDg2bc6r5MBxZVA6Tjx0mfwUDI9vz0ypkCAwEAAQ==";

    private static final String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAq6Ru6EsmEMba+slVIQ48xWJoDZ3KEqa64mHjhIm91nkaUskAtx7+mAODZtzqvkwHFlUDpOPHSZ/BQMj2/PTKmQIDAQABAkEAnkm4FfUnl5UrYNfG8AMHPChyOQxozCaCdj876IB2V5AzqlNV6lTpCh7veGz/hhCdfdA81c9Sbi6FoDlGJkfueQIhAPVt6Eb4bsZMAVO7yYIbCNFyzTfiLBIOzffExyxf51KnAiEAswj0JCXxoYaBGW7tHikNXdnkpjKkbQEBiH8WOR4U4L8CIQCdm5T8bnGEui5n/UHsYTwKdPTAnGe8uPEf2agmIPhGJQIhAIoFsRHdHrcD1qsg1TSXOXLM9HUcPZ67U89DCoLmKfpJAiBvKHPQPgw6DY5P3C/Ni5liSctCjzAZD4zedMaba7aghw==";

    @Test
    public void keyPair() {
        var keyPair = getKeyPair();

        String publicKey = DigestUtil.encodeBase64(keyPair.getPublic().getEncoded());
        String privateKey = DigestUtil.encodeBase64(keyPair.getPrivate().getEncoded());
        log.info("publicKey:{}", publicKey);
        log.info("privateKey:{}", privateKey);

    }

    @Test
    public void test() {
        var privateKey1 = loadPrivateKey(privateKey);
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.RS256, privateKey1)
                .setIssuer("www.php25.com")
                .setIssuedAt(new Date())
                .setHeaderParam("authorities", List.of("admin"))
                .setSubject("jack")
                .compact();
        log.info("jwt:{}", jwt);
    }

    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception var1) {
            throw new RuntimeException("出错啦!", var1);
        }
    }

    private static PublicKey loadPublicKey(String pubStr) {
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

    private static PrivateKey loadPrivateKey(String priStr) {
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
