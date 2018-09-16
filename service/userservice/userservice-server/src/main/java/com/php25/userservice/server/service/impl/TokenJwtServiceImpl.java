package com.php25.userservice.server.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.redis.RedisService;
import com.php25.userservice.server.service.TokenJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Slf4j
@Transactional
@Service
@Primary
public class TokenJwtServiceImpl implements TokenJwtService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisService redisService;

    private static final String secret = "%TGB6yhn";
    private static final String REDIS_JWT = "jwt:";
    private static final Long REDIS_KEY_EXPIRE_TIME = 1800L;
    @Autowired
    private IdGeneratorService idGeneratorService;

    @Override
    public String getToken(String key) {
        Assert.hasText(key, "key不能为空");
        String id = idGeneratorService.getModelPrimaryKey();
        String header = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
        //jwt token有效期30分钟
        redisService.set(REDIS_JWT + id, key, REDIS_KEY_EXPIRE_TIME);

        String payload = String.format("{\"iss\":\"%s\"}", id);
        String secret = this.getJwtSecret();
        try {
            Map<String, Object> maps = objectMapper.readValue(header, new TypeReference<Map<String, Object>>() {
            });
            String compactJws = Jwts.builder().setHeader(maps).setPayload(payload).signWith(SignatureAlgorithm.HS256, secret).compact();
            return compactJws;
        } catch (Exception e) {
            throw new RuntimeException("生成jwt的token失败", e);
        }
    }

    @Override
    public Boolean verifyToken(String token) {
        if (!Jwts.parser().isSigned(token)) {
            return false;
        }
        Claims claims = parseJwtToken(token);
        String id = claims.getIssuer();
        if (redisService.exists(REDIS_JWT + id)) {
            //存在id,刷新id的有效时间
            redisService.expire(REDIS_JWT + id, REDIS_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        } else {
            //不存在
            return false;
        }
    }

    @Override
    public String getKeyByToken(String token) {
        String id = parseJwtToken(token).getIssuer();
        String key = null;
        if (redisService.exists(REDIS_JWT + id)) {
            key = redisService.get(REDIS_JWT + id, String.class);
        }
        return key;
    }

    @Override
    public Boolean cleanToken(String token) {
        if (this.verifyToken(token)) {
            Claims claims = parseJwtToken(token);
            String id = claims.getIssuer();
            redisService.remove(REDIS_JWT + id);
            return true;
        }
        return false;
    }

    private Claims parseJwtToken(String jwt) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(this.getJwtSecret()).parseClaimsJws(jwt);
        return claims.getBody();
    }

    private String getJwtSecret() {
        return DigestUtil.encodeBase64(secret.getBytes());
    }
}
