package com.php25.userservice.server.service;

import java.util.Map;

/**
 * 手机app免登入token实现，遵循Oauth2密码模式规范
 *
 * @author penghuiping
 * @timer 2017/1/24.
 */
public interface TokenService<T> {

    /**
     * 根据用户 获取 token
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Map<String, String> generateToken(T obj);

    /**
     * 根据refreshToken 获取token
     *
     * @param refreshToken
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Map<String, String> getToken(String refreshToken);

    /**
     * 检查token值是否有效
     *
     * @param token
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Boolean checkTokenValidation(String token, Class<T> cls);

    /**
     * 获取access token 过期时间
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Long getAccessTokenExpireTime();

    /**
     * 获取refresh token 过期时间
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Long getRefreshTokenExpireTime();

    /**
     * 根据token获取对象
     */
    T getObjByToken(String token, Class<T> cls);


    /**
     * 清空token
     *
     * @param obj
     * @return
     */
    Boolean cleanToken(T obj);

}


