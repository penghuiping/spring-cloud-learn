package com.php25.userservice.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.userservice.client.rest.TokenRest;
import com.php25.userservice.server.service.CustomerService;
import com.php25.userservice.server.service.TokenService;
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
    TokenService<String> tokenService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 根据手机用户获取token
     *
     * @param objId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTokenByObjId")
    public Map<String, String> getTokenByObjId(@NotBlank @RequestParam("objId") String objId) {
        return tokenService.generateToken(objId);
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
        return tokenService.checkTokenValidation(token, String.class);
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
     * 根据token获取用户对象id
     */
    @Deprecated
    @RequestMapping(value = "/getObjIdByToken")
    public String getObjIdByToken(@NotBlank @RequestParam("token") String token) {
        return tokenService.getObjByToken(token, String.class);
    }


    /**
     * 清除token信息
     *
     * @return
     */
    @RequestMapping(value = "/cleanToken")
    public Boolean cleanToken(@NotBlank @RequestParam("objId") String objId) {
        return tokenService.cleanToken(objId);
    }

}
