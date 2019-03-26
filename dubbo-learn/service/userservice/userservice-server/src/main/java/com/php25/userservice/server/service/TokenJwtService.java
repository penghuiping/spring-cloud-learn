package com.php25.userservice.server.service;

/**
 * Created by penghuiping on 2018/3/15.
 */
public interface TokenJwtService {

    /**
     * 通过key生成token
     *
     * @return
     */
    public String getToken(String key);

    /**
     * 通过token反向获取key
     *
     * @param token
     * @return
     */
    public String getKeyByToken(String token);


    /**
     * 清除jwt token
     * @param token
     * @return
     */
    public Boolean cleanToken(String token);

    /**
     * 验证token是否合法
     *
     * @param token
     * @return
     */
    public Boolean verifyToken(String token);

}
