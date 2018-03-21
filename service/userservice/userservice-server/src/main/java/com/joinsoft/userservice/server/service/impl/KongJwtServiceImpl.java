package com.joinsoft.userservice.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinsoft.common.service.RedisService;
import com.joinsoft.common.util.TimeUtil;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.client.dto.JwtCredentialDto;
import com.joinsoft.userservice.server.service.CustomerService;
import com.joinsoft.userservice.server.service.KongJwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
    private Integer jwtTokenExpireTime;

    private static OkHttpClient okHttpClient = new OkHttpClient();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CustomerService customerService;

    @Override
    public String generateJwtCustomerId(CustomerDto customerDto) {
        Assert.notNull(customerDto, "customerDto不能为null");
        //生成jwtCustomerId
        String jwtCustomerId = customerDto.getId();
        return jwtCustomerId;
    }

    @Override
    public void createJwtCustomer(String jwtCustomerId) {
        Assert.hasLength(jwtCustomerId, "jwtCustomer不能为空");
        try {
            String url = kongAdminBaseUrl + "/consumers";
            Request request = new Request.Builder().url(url + "/" + jwtCustomerId).get().build();
            Response response = okHttpClient.newCall(request).execute();
            if (200 == response.code()) {
                return;
            }

            RequestBody body = new FormBody.Builder().add("username", jwtCustomerId).build();
            request = new Request.Builder().url(url).put(body).build();
            response = okHttpClient.newCall(request).execute();
            if (201 == response.code() || 200 == response.code()) {
                Logger.getLogger(KongJwtServiceImpl.class).debug("创建或者更新成功");
            } else {
                throw new RuntimeException("创建kong的customer对象失败");
            }
        } catch (IOException e) {
            throw new RuntimeException("创建kong的customer对象失败", e);
        }
    }

    @Override
    public JwtCredentialDto generateJwtCredential(String jwtCustomerId) {
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
            throw new RuntimeException("生成jwt相关的证书失败", e);
        }
    }

    @Override
    public String generateJwtToken(JwtCredentialDto jwtCredentialDto) {
        Assert.notNull(jwtCredentialDto, "jwtCredentialDto不能为null");
        String header = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
        String payload = String.format("{\"iss\":\"%s\",\"exp\":%s}", jwtCredentialDto.getKey(), TimeUtil.offsiteDate(new Date(), Calendar.SECOND, jwtTokenExpireTime).getTime() / 1000);
        String secret = jwtCredentialDto.getSecret();

        try {
            Map<String, Object> maps = objectMapper.readValue(header, Map.class);
            String compactJws = Jwts.builder().setHeader(maps).setPayload(payload).signWith(SignatureAlgorithm.HS256, secret).compact();
            return compactJws;
        } catch (Exception e) {
            throw new RuntimeException("生成jwt的token失败", e);
        }
    }

    @Override
    public CustomerDto getByJwtCustomerId(String jwtCustomerId) {
        Assert.hasLength(jwtCustomerId, "jwtCustomerId不能为空");
        CustomerDto customerDto = redisService.get(jwtCustomerId, CustomerDto.class);
        if (null == customerDto) {
            customerDto = customerService.findOne(jwtCustomerId);
            redisService.set(jwtCustomerId, customerDto, 3600 * 24l);
        }
        return customerDto;
    }

    @Override
    public void cleanJwtToken(String jwtCustomerId) {
        Assert.hasLength(jwtCustomerId, "jwtCustomerId不能为空");
        try {
            //kong清除jwtCustomerId
            String url = kongAdminBaseUrl + "/consumers/" + jwtCustomerId;
            Request request = new Request.Builder().url(url).delete().build();
            Response response = okHttpClient.newCall(request).execute();
            if (204 != response.code()) {
                throw new RuntimeException("清除jwt的token失败");
            }
        } catch (IOException e) {
            throw new RuntimeException("清除jwt的token失败", e);
        }


    }
}
