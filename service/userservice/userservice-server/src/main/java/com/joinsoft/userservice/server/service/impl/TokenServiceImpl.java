package com.joinsoft.userservice.server.service.impl;

import com.php25.common.service.RedisService;
import com.php25.common.service.impl.RedisServiceImpl;
import com.php25.common.util.DigestUtil;
import com.php25.common.util.StringUtil;
import com.php25.common.util.TimeUtil;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.server.service.CustomerService;
import com.joinsoft.userservice.server.service.IdGeneratorService;
import com.joinsoft.userservice.server.service.TokenService;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机app免登入token实现，遵循Oauth2密码模式规范
 *
 * @author penghuiping
 * @timer 2017/1/24.
 */
@Service
@Primary
public class TokenServiceImpl implements TokenService {

    private static final Long access_token_expire_time = 3600l;
    private static final Long refresh_token_expire_time = 3600 * 24 * 7l;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private CustomerService customerService;


    /**
     * 根据用户，获取access token
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Map<String, String> getToken(CustomerDto customer) {
        if (null == customer) {
            return null;
        }

        //生成token
        String token = "t" + new String(DigestUtil.bytes2hex(DigestUtil.MD5(idGeneratorService.generateLoginToken())));
        String refreshToken = new String(DigestUtil.bytes2hex(DigestUtil.MD5(idGeneratorService.generateLoginToken())));

        //放入redis缓存
        //access_token有效期为1小时
        //refresh_token有效期为7天
        redisService.set(token, customer, access_token_expire_time);
        redisService.set(refreshToken, customer, refresh_token_expire_time);

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", token);
        map.put("refresh_token", refreshToken);
        map.put("expires_in", access_token_expire_time + "");
        map.put("refresh_token_expires_in", refresh_token_expire_time + "");
        return map;
    }

    /**
     * 根据refreshToken 获取token
     * <p>
     * 每个refreshToken，每天最多获取10次
     *
     * @param refreshToken
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Map<String, String> getToken(String refreshToken) {
        CustomerDto obj = redisService.get(refreshToken, CustomerDto.class);

        if (null == obj) {
            return null;
        }

        RAtomicLong rAtomicLong = ((RedisServiceImpl) redisService).getRedission().getAtomicLong(refreshToken.substring(8, 24));
        rAtomicLong.expireAt(TimeUtil.getBeginTimeOfDay(TimeUtil.offsiteDay(new Date(), 1)));

        if ((rAtomicLong.isExists() && rAtomicLong.get() >= 10l) || (rAtomicLong.isExists() && rAtomicLong.get() < 0l)) {
            return null;

        }


        if (!rAtomicLong.isExists()) {
            rAtomicLong.set(0l);
        }

        rAtomicLong.getAndIncrement();
        String token = "t" + new String(DigestUtil.bytes2hex(DigestUtil.MD5(idGeneratorService.generateLoginToken())));
        //放入redis缓存
        redisService.set(token, obj, access_token_expire_time);
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", token);
        map.put("refresh_token", refreshToken);
        map.put("expires_in", access_token_expire_time + "");
        map.put("refresh_token_expires_in", refresh_token_expire_time + "");
        return map;

    }


    /**
     * 检查token值是否有效
     *
     * @param token
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Boolean checkTokenValidation(String token) {
        if (!StringUtil.isBlank(token) && token.length() == 33 && token.startsWith("t")) {
            CustomerDto temp = redisService.get(token, CustomerDto.class);
            if (null != temp) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置token过期
     *
     * @param customerId
     * @return
     */
    @Override
    public Boolean setTokenExpire(String customerId) {
        CustomerDto customerDto = customerService.findOne(customerId);
        Map<String, String> map = getToken(customerDto);
        String token = (String) map.get("access_token");
        String refreshToken = (String) map.get("refresh_token");
        if (null != token) {
            redisService.remove(token);
        }

        if (null != refreshToken) {
            redisService.remove(refreshToken);
        }
        return true;
    }

    /**
     * 获取access token 过期时间
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Long getAccessTokenExpireTime() {
        return access_token_expire_time;
    }

    /**
     * 获取refresh token 过期时间
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Long getRefreshTokenExpireTime() {
        return refresh_token_expire_time;
    }

    @Override
    public CustomerDto getUserObject(String token) {
        CustomerDto temp = redisService.get(token, CustomerDto.class);
        return temp;
    }

    @Override
    public CustomerDto getCustomerDtoByToken(String token) {
        return redisService.get(token, CustomerDto.class);
    }

    @Override
    public Boolean cleanToken(String token, String refreshToken) {
        redisService.remove(token, refreshToken);
        return true;
    }

}


