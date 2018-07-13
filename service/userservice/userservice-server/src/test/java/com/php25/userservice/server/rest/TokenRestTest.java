package com.php25.userservice.server.rest;

import com.php25.common.service.RedisService;
import com.php25.userservice.client.rest.TokenRest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


public class TokenRestTest extends BaseRestTest {

    private static final String objId = "123123123";

    @Autowired
    TokenRest tokenRest;

    @Autowired
    RedisService redisService;

    @Value(value = "${app.oath2.access_token.expire_time:#{3600l}}")
    private Long access_token_expire_time;

    @Value(value = "${app.oath2.refresh_token.expire_time:#{3600 * 24 * 7l}}")
    private Long refresh_token_expire_time;

    @Test
    public void test() {
        Map<String, String> map = tokenRest.getTokenByObjId(objId);

        String accessToken = map.get("access_token");
        String refreshToken = map.get("refresh_token");

        logger.info(map.toString());

        Assert.assertEquals("验证accessToken的失效时间是配置的失效时间失败", tokenRest.getAccessTokenExpireTime(), access_token_expire_time);
        Assert.assertEquals("验证refreshToken的失效时间是配置的失效时间失败", tokenRest.getRefreshTokenExpireTime(), refresh_token_expire_time);

        Assert.assertTrue("验证accessToken是有效的失败", tokenRest.checkTokenValidation(accessToken));

        Map<String, String> map1 = tokenRest.getTokenByRefreshToken(refreshToken);

        String accessToken1 = map1.get("access_token");

        Assert.assertNotEquals("验证accessToken与accessToken1不相同失败", accessToken, accessToken1);

        String refreshToken1 = map1.get("refresh_token");

        Assert.assertEquals("验证refreshToken与refreshToken1相同失败", refreshToken, refreshToken1);

        Assert.assertFalse("验证accessToken变为失效失败", tokenRest.checkTokenValidation(accessToken));

        Assert.assertTrue("根据对象id清除token与refreshToken成功失败", tokenRest.cleanToken(objId));

        Assert.assertNull("accessToken1失效失败", redisService.get(accessToken1, String.class));
        Assert.assertNull("refreshToken1失效失败", redisService.get(refreshToken1, String.class));

    }
}
