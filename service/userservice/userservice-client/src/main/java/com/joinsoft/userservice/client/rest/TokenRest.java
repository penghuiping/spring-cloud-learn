package com.joinsoft.userservice.client.rest;

import com.joinsoft.userservice.client.dto.CustomerDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by penghuiping on 2017/3/8.
 */
public interface TokenRest {

    /**
     * 根据手机用户获取token
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTokenByCustomer")
    public Map<String, String> getTokenByCustomer(@NotBlank @RequestParam("customerId") String customerId);

    /**
     * 通过refreshtoken重新获取一个token
     *
     * @param refreshToken
     * @return
     */
    @RequestMapping(value = "/getTokenByRefreshToken")
    public Map<String, String> getTokenByRefreshToken(@NotBlank @RequestParam("refreshToken") String refreshToken);

    /**
     * 检测token值是否有效
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/checkTokenValidation")
    public Boolean checkTokenValidation(@NotBlank @RequestParam("token") String token);

    /**
     * 设置token过期失效
     *
     * @param customerId
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/setTokenExpire")
    public Boolean setTokenExpire(@NotBlank @RequestParam("customerId") String customerId);

    /**
     * 获取token的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getAccessTokenExpireTime")
    public Long getAccessTokenExpireTime();

    /**
     * 获取refreshtoken的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getRefreshTokenExpireTime")
    public Long getRefreshTokenExpireTime();


    /**
     * 根据token获取用户对象
     */
    @Deprecated
    @RequestMapping(value = "/getUserObject")
    public CustomerDto getUserObject(@NotBlank @RequestParam("token") String token);


    /**
     * 根据token获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/getCustomerByToken")
    public CustomerDto getCustomerByToken(@NotBlank @RequestParam("token") String token);

    /**
     * 清除token信息
     *
     * @return
     */
    @RequestMapping(value = "/cleanToken")
    public Boolean cleanToken(@NotBlank @RequestParam("token") String token, @NotBlank @RequestParam("refreshToken") String refreshToken);

}
