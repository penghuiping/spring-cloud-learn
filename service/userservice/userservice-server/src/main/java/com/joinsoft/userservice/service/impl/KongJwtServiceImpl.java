package com.joinsoft.userservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinsoft.common.exception.JsonException;
import com.joinsoft.common.service.RedisService;
import com.joinsoft.common.util.DigestUtil;
import com.joinsoft.userservice.dto.CustomerDto;
import com.joinsoft.userservice.dto.JwtCredentialDto;
import com.joinsoft.userservice.service.IdGeneratorService;
import com.joinsoft.userservice.service.KongJwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Transactional
@Service
@Primary
public class KongJwtServiceImpl implements KongJwtService {

    @Value("${app.kong.adminBaseUrl:null}")
    private String kongAdminBaseUrl;

    @Value("${app.kong.jwtTokenExpireTime:3600}")
    private Long jwtTokenExpireTime;

    private static OkHttpClient okHttpClient = new OkHttpClient();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private RedisService redisService;

    @Override
    public String generateJwtCustomerId(CustomerDto customerDto) throws JsonException {
        Assert.notNull(customerDto, "customerDto不能为null");
        //生成jwtCustomerId
        String jwtCustomerId = "t" + new String(DigestUtil.bytes2hex(DigestUtil.MD5(idGeneratorService.generateLoginToken())));
        //放入redis缓存,设置过期时间
        redisService.set(jwtCustomerId, customerDto, jwtTokenExpireTime);
        return jwtCustomerId;
    }

    @Override
    public void createJwtCustomer(String jwtCustomerId) throws JsonException {
        Assert.hasLength(jwtCustomerId, "jwtCustomer不能为空");
        try {
            String url = kongAdminBaseUrl + "/consumers";
            RequestBody body = new FormBody.Builder().add("username", jwtCustomerId).build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = okHttpClient.newCall(request).execute();
            if (201 != response.code()) {
                throw new JsonException("创建kong的customer对象失败");
            }
        } catch (IOException e) {
            throw new JsonException("创建kong的customer对象失败", e);
        }
    }

    @Override
    public JwtCredentialDto generateJwtCredential(String jwtCustomerId) throws JsonException {
        Assert.hasLength(jwtCustomerId, "jwtCustomer不能为空");
        //创建jwt相关的参数
        try {
            String url = kongAdminBaseUrl + "/consumers/" + jwtCustomerId + "/jwt";
            RequestBody body = new FormBody.Builder().build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = okHttpClient.newCall(request).execute();
            JwtCredentialDto jwtCredentialDto = objectMapper.readValue(response.body().string(), JwtCredentialDto.class);
            return jwtCredentialDto;
        } catch (IOException e) {
            throw new JsonException("生成jwt相关的证书失败", e);
        }
    }

    @Override
    public String generateJwtToken(JwtCredentialDto jwtCredentialDto) throws JsonException {
        Assert.notNull(jwtCredentialDto, "jwtCredentialDto不能为null");
        String header = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
        String payload = String.format("{\"iss\":\"%s\"}", jwtCredentialDto.getKey());
        String secret = jwtCredentialDto.getSecret();

        try {
            Map<String, Object> maps = objectMapper.readValue(header, Map.class);
            String compactJws = Jwts.builder().setHeader(maps).setPayload(payload).signWith(SignatureAlgorithm.HS256, secret).compact();
            return compactJws;
        } catch (Exception e) {
            throw new JsonException("生成jwt的token失败", e);
        }
    }
}
