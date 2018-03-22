package com.joinsoft.userservice.server.service;

import com.joinsoft.userservice.client.dto.CustomerDto;

import java.util.Map;

/**
 * 手机app免登入token实现，遵循Oauth2密码模式规范
 *
 * @author penghuiping
 * @timer 2017/1/24.
 */
public interface TokenService {

    /**
     * 根据用户 获取 token
     *
     * @return
     * @author penghuiping
     * @timer 2017/1/24
     */
    Map<String, String> getToken(CustomerDto customer);

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
    Boolean checkTokenValidation(String token);

    /**
     * 设置token过期
     *
     * @param customerId
     * @return
     */
    Boolean setTokenExpire(String customerId);


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
     * 根据token获取用户对象
     */
    CustomerDto getUserObject(String token);


    /**
     * 清空token
     *
     * @param token
     * @param refreshToken
     * @return
     */
    Boolean cleanToken(String token, String refreshToken);


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    CustomerDto getCustomerDtoByToken(String token);
}


