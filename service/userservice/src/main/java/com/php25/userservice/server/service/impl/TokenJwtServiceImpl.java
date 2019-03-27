package com.php25.userservice.server.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.redis.RedisService;
import com.php25.userservice.server.service.TokenJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Slf4j
@Service
@Primary
public class TokenJwtServiceImpl implements TokenJwtService {
    @Autowired
    private RedisService redisService;

    @Value("${jwt_secret}")
    private String secret;

    private static final String REDIS_JWT = "jwt:";

    @Value("${jwt_expire_time}")
    private static Long REDIS_KEY_EXPIRE_TIME;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Override
    public String getToken(String userId,Map<String,Object> value) {
        Assert.hasText(userId, "key不能为空");
        String id = idGeneratorService.getModelPrimaryKey();
        String header = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
        //jwt token有效期30分钟
        String jwtId = idGeneratorService.getModelPrimaryKey();
        value.put("jwtId",jwtId);
        redisService.set(REDIS_JWT + id, value, REDIS_KEY_EXPIRE_TIME);
        String secret = this.getJwtSecret();
        try {
            Map<String, Object> maps = JsonUtil.fromJson(header, new TypeReference<Map<String, Object>>() {
            });
            return Jwts.builder().setHeader(maps).setIssuer(userId).setIssuedAt(new Date()).setId(jwtId).signWith(SignatureAlgorithm.HS256, secret).compact();
        } catch (Exception e) {
            throw new RuntimeException("生成jwt的token失败", e);
        }
    }

    @Override
    public Boolean verifyToken(String token) {
        try {
            if (!Jwts.parser().isSigned(token)) {
                return false;
            }
            Claims claims = parseJwtToken(token);
            String id = claims.getIssuer();
            String key = REDIS_JWT + id;
            if (redisService.exists(key)) {
                //存在id,刷新id的有效时间
                redisService.expire(REDIS_JWT + id, REDIS_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
                Map<String,Object> jwtUserVo = redisService.get(key, new TypeReference<Map<String, Object>>() {
                });
                String jwtId = claims.getId();
                if(!jwtUserVo.get("jwtId").equals(jwtId)) {
                    log.info("redis中不存在对应的jwt");
                    //不存在
                    return false;
                }
                //存在id,刷新id的有效时间
                redisService.expire(key, REDIS_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
                return true;
            } else {
                //不存在
                return false;
            }
        }catch (Exception e) {
            log.error("认证jwt解析失败", e);
            return false;
        }
    }

    @Override
    public String getKeyByToken(String token) {
        return parseJwtToken(token).getIssuer();
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


    @Override
    public Map<String,Object> getValueByToken(String token) {
        String id = parseJwtToken(token).getIssuer();
        if (redisService.exists(REDIS_JWT+ id)) {
            return redisService.get(REDIS_JWT + id, new TypeReference<Map<String, Object>>() {
            }) ;
        } else {
            return null;
        }
    }

    private Claims parseJwtToken(String jwt) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(this.getJwtSecret()).parseClaimsJws(jwt);
        return claims.getBody();
    }

    private String getJwtSecret() {
        log.info("jwt_secret为:{}",this.secret);
        return DigestUtil.encodeBase64(secret.getBytes());
    }
}
