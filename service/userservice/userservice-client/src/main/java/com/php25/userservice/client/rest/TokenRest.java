package com.php25.userservice.client.rest;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by penghuiping on 2017/3/8.
 */
public interface TokenRest {


    /**
     * 根据对象id获取token
     *
     * @param objId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTokenByObjId")
    Map<String, String> getTokenByObjId(@NotBlank @RequestParam("objId") String objId);

    /**
     * 通过refreshtoken重新获取一个token
     *
     * @param refreshToken
     * @return
     */
    @RequestMapping(value = "/getTokenByRefreshToken")
    Map<String, String> getTokenByRefreshToken(@NotBlank @RequestParam("refreshToken") String refreshToken);

    /**
     * 检测token值是否有效
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/checkTokenValidation")
    Boolean checkTokenValidation(@NotBlank @RequestParam("token") String token);


    /**
     * 获取token的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getAccessTokenExpireTime")
    Long getAccessTokenExpireTime();

    /**
     * 获取refreshtoken的过期时间
     *
     * @return
     */
    @RequestMapping(value = "/getRefreshTokenExpireTime")
    Long getRefreshTokenExpireTime();


    /**
     * 根据token获取对象id
     */
    @Deprecated
    @RequestMapping(value = "/getObjIdByToken")
    String getObjIdByToken(@NotBlank @RequestParam("token") String token);

    /**
     * 清除token信息
     *
     * @return
     */
    @RequestMapping(value = "/cleanToken")
    Boolean cleanToken(@NotBlank @RequestParam("objId") String objId);

}
