package com.php25.usermicroservice.server.service;

import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * Created by penghuiping on 2018/3/15.
 */
public interface TokenJwtService extends InitializingBean {

    /**
     * 通过userId生成token
     * @param userId
     * @param value
     * @return
     */
    public String getToken(String userId, Map<String, Object> value);

    /**
     * 通过token反向获取userId
     *
     * @param token
     * @return
     */
    public String getKeyByToken(String token);

    /**
     * 通过token反向获取value
     *
     * @param token
     * @return
     */
    public Map<String,Object> getValueByToken(String token);


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
