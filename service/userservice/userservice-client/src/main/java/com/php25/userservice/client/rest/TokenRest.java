package com.php25.userservice.client.rest;

import feign.Param;
import feign.RequestLine;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/**
 * Created by penghuiping on 2017/3/8.
 */
public interface TokenRest {

    String baseUri = "/token";

    /**
     * 根据对象id获取token
     * 根据手机用户获取token
     *
     * @param objId
     * @return
     * @throws Exception
     */
    @RequestLine("GET " + baseUri + "/getTokenByObjId?objId={objId}")
    public Map<String, String> getTokenByObjId(@NotBlank @Param("objId") String objId);

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
    @RequestLine("GET " + baseUri + "/checkTokenValidation?token={token}")
    public Boolean checkTokenValidation(@NotBlank @Param("token") String token);


    /**
     * 获取token的过期时间
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/getAccessTokenExpireTime")
    public Long getAccessTokenExpireTime();

    /**
     * 获取refreshtoken的过期时间
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/getRefreshTokenExpireTime")
    public Long getRefreshTokenExpireTime();


    /**
     * 根据token获取对象id
     * 根据token获取用户对象
     */
    @RequestLine("GET " + baseUri + "/getObjIdByToken?token={token}")
    public String getObjIdByToken(@NotBlank @Param("token") String token);


    /**
     * 根据token获取对象id
     * 根据token获取用户对象
     */
    @RequestLine("GET " + baseUri + "/cleanToken?objId={objId}")
    public Boolean cleanToken(@NotBlank @Param("objId") String objId);

}
