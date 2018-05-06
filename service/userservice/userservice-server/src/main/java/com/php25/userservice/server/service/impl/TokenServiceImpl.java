package com.php25.userservice.server.service.impl;

import com.php25.common.service.IdGeneratorService;
import com.php25.common.service.RedisService;
import com.php25.common.service.impl.RedisServiceImpl;
import com.php25.common.util.StringUtil;
import com.php25.common.util.TimeUtil;
import com.php25.userservice.server.service.TokenService;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
public class TokenServiceImpl<T> implements TokenService<T> {

    @Value(value = "${access_token_expire_time:3600}")
    private Long access_token_expire_time;

    @Value(value = "${refresh_token_expire_time:#{3600 * 24 * 7}}")
    private Long refresh_token_expire_time;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    /**
     * 根据用户，获取access token
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    public Map<String, String> generateToken(T obj) {
        Assert.notNull(obj, "obj不能为null");
        if (obj instanceof String || obj instanceof Long) {
            //生成token
            String token = "t" + idGeneratorService.getModelPrimaryKey();
            String refreshToken = idGeneratorService.getModelPrimaryKey();

            //放入redis缓存
            //access_token有效期为1小时
            //refresh_token有效期为7天
            redisService.set(token, obj, access_token_expire_time);
            redisService.set(refreshToken, obj, refresh_token_expire_time);

            //生产反向回溯依赖关系
            String key = obj + "";
            redisService.set(key, token + "," + refreshToken);

            Map<String, String> map = new HashMap<String, String>();
            map.put("access_token", token);
            map.put("refresh_token", refreshToken);
            map.put("expires_in", access_token_expire_time + "");
            map.put("refresh_token_expires_in", refresh_token_expire_time + "");
            return map;
        } else {
            throw new RuntimeException("obj只能是String或者Long类型");
        }
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
        Assert.hasLength(refreshToken, "refreshToken不能为空");
        String obj = redisService.get(refreshToken, String.class);

        if (null == obj) {
            return null;
        }

        //失效原来的token
        String value = redisService.get(obj, String.class);
        if (!StringUtil.isBlank(value)) {
            String[] tmp = value.split(",");
            String token = tmp[0];
            redisService.remove(token);
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
        String token = "t" + idGeneratorService.getModelPrimaryKey();

        //生产反向回溯依赖关系
        String key = obj + "";
        redisService.set(key, token + "," + refreshToken);

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
    public Boolean checkTokenValidation(String token, Class<T> cls) {
        Assert.hasLength(token, "token不能为空");
        if (!StringUtil.isBlank(token) && token.length() == 33 && token.startsWith("t")) {
            T temp = redisService.get(token, cls);
            return null != temp;
        }
        return false;
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
    public T getObjByToken(String token, Class<T> cls) {
        return redisService.get(token, cls);
    }

    @Override
    public Boolean cleanToken(T obj) {
        if (obj instanceof String || obj instanceof Long) {
            String key = obj + "";
            String value = redisService.get(key, String.class);
            if (!StringUtil.isBlank(value)) {
                String[] tmp = value.split(",");
                String token = tmp[0];
                String refreshToken = tmp[1];
                redisService.remove(token, refreshToken);
            }
            return true;
        } else {
            throw new RuntimeException("obj只能是String或者Long类型");
        }
    }

}


