package com.php25.userservice.server.rpc;

import com.php25.userservice.client.rpc.TokenJwtRpc;
import com.php25.userservice.server.service.TokenJwtService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 13:36
 * @Description:
 */
@com.alibaba.dubbo.config.annotation.Service
public class TokenJwtRpcImpl implements TokenJwtRpc {

    @Autowired
    private TokenJwtService tokenJwtService;

    @Override
    public String getToken(String key) {
        return tokenJwtService.getToken(key);
    }

    @Override
    public String getKeyByToken(String token) {
        return tokenJwtService.getKeyByToken(token);
    }

    @Override
    public Boolean verifyToken(String token) {
        return tokenJwtService.verifyToken(token);
    }

    @Override
    public Boolean cleanToken(String token) {
        return tokenJwtService.cleanToken(token);
    }
}
