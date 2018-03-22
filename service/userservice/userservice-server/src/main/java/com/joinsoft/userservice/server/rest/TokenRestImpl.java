package com.joinsoft.userservice.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.client.rest.TokenRest;
import com.joinsoft.userservice.server.service.CustomerService;
import com.joinsoft.userservice.server.service.TokenService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by penghuiping on 2017/3/8.
 */
@Validated
@RestController
@RequestMapping("/token")
public class TokenRestImpl implements TokenRest {

    @Autowired
    TokenService tokenService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 根据手机用户获取token
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTokenByCustomer")
    public Map<String, String> getTokenByCustomer(@NotBlank @RequestParam("customerId") String customerId) {
        CustomerDto customerDto = customerService.findOne(customerId);
        return tokenService.getToken(customerDto);
    }

    /**
     * 通过refreshtoken重新获取一个token
     *
     * @param refreshToken
     * @return
     */
    @RequestMapping(value = "/getTokenByRefreshToken")
    public Map<String, String> getTokenByRefreshToken(@NotBlank @RequestParam("refreshToken") String refreshToken) {
        return tokenService.getToken(refreshToken);
    }

    /**
     * 检测token值是否有效
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/checkTokenValidation")
    public Boolean checkTokenValidation(@NotBlank @RequestParam("token") String token) {
        return tokenService.checkTokenValidation(token);
    }

    /**
     * 设置token过期失效
     *
     * @param customerId
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/setTokenExpire")
    public Boolean setTokenExpire(@NotBlank @RequestParam("customerId") String customerId) {
        return tokenService.setTokenExpire(customerId);
    }

    /**
     * 获取token的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getAccessTokenExpireTime")
    public Long getAccessTokenExpireTime() {
        return tokenService.getAccessTokenExpireTime();
    }

    /**
     * 获取refreshtoken的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getRefreshTokenExpireTime")
    public Long getRefreshTokenExpireTime() {
        return tokenService.getRefreshTokenExpireTime();
    }


    /**
     * 根据token获取用户对象
     */
    @Deprecated
    @RequestMapping(value = "/getUserObject")
    public CustomerDto getUserObject(@NotBlank @RequestParam("token") String token) {
        return tokenService.getUserObject(token);
    }


    /**
     * 根据token获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/getCustomerByToken")
    public CustomerDto getCustomerByToken(@NotBlank @RequestParam("token") String token) {
        return tokenService.getCustomerDtoByToken(token);
    }

    /**
     * 清除token信息
     *
     * @return
     */
    @RequestMapping(value = "/cleanToken")
    public Boolean cleanToken(@NotBlank @RequestParam("token") String token, @NotBlank @RequestParam("refreshToken") String refreshToken) {
        return tokenService.cleanToken(token, refreshToken);
    }

}
