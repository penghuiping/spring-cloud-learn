package com.joinsoft.userservice.client.rest;

import com.joinsoft.userservice.client.dto.CustomerDto;
import feign.Param;
import feign.RequestLine;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by penghuiping on 2017/3/8.
 */
public interface TokenRest {

    String baseUri = "/token";

    /**
     * 根据手机用户获取token
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestLine("GET " + baseUri + "/getTokenByCustomer?customerId={customerId}")
    public Map<String, String> getTokenByCustomer(@NotBlank @Param("customerId") String customerId);

    /**
     * 通过refreshtoken重新获取一个token
     *
     * @param refreshToken
     * @return
     */
    @RequestLine("GET " + baseUri + "/getTokenByRefreshToken?refreshToken={refreshToken}")
    public Map<String, String> getTokenByRefreshToken(@NotBlank @Param("refreshToken") String refreshToken);

    /**
     * 检测token值是否有效
     *
     * @param token
     * @return
     */
    @RequestMapping("GET " + baseUri + "/checkTokenValidation?token={token}")
    public Boolean checkTokenValidation(@NotBlank @Param("token") String token);

    /**
     * 设置token过期失效
     *
     * @param customerId
     * @return
     */
    @Deprecated
    @RequestMapping("GET " + baseUri + "/setTokenExpire?customerId={customerId}")
    public Boolean setTokenExpire(@NotBlank @Param("customerId") String customerId);

    /**
     * 获取token的过期时间
     *
     * @return
     */
    @RequestMapping("GET " + baseUri + "/getAccessTokenExpireTime")
    public Long getAccessTokenExpireTime();

    /**
     * 获取refreshtoken的过期时间
     *
     * @return
     */
    @RequestMapping("GET " + baseUri + "/getRefreshTokenExpireTime")
    public Long getRefreshTokenExpireTime();


    /**
     * 根据token获取用户对象
     */
    @Deprecated
    @RequestMapping("GET " + baseUri + "/getUserObject?token={token}")
    public CustomerDto getUserObject(@NotBlank @Param("token") String token);


    /**
     * 根据token获取用户信息
     *
     * @return
     */
    @RequestMapping("GET " + baseUri + "/getCustomerByToken?token={token}")
    public CustomerDto getCustomerByToken(@NotBlank @Param("token") String token);

    /**
     * 清除token信息
     *
     * @return
     */
    @RequestMapping("GET " + baseUri + "/cleanToken?token={token}&refreshToken={refreshToken}")
    public Boolean cleanToken(@NotBlank @Param("token") String token, @NotBlank @Param("refreshToken") String refreshToken);

}
