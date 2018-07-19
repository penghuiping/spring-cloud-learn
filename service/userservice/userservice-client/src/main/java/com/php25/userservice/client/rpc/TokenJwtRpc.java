package com.php25.userservice.client.rpc;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 13:37
 * @Description:
 */
public interface TokenJwtRpc {

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
     * 验证token是否合法
     *
     * @param token
     * @return
     */
    public Boolean verifyToken(String token);

    /**
     * 清除jwt token
     *
     * @param token
     * @return
     */
    public Boolean cleanToken(String token);


}
