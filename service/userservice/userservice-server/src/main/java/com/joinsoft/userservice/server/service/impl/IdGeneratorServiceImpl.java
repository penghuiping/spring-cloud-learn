package com.joinsoft.userservice.server.service.impl;

import com.joinsoft.common.util.RandomUtil;
import com.joinsoft.common.util.TimeUtil;
import com.joinsoft.userservice.client.constant.Constant;
import com.joinsoft.userservice.server.service.IdGeneratorService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by penghuiping on 5/15/15.
 */
@Service
@Primary
public class IdGeneratorServiceImpl implements IdGeneratorService {
    /**
     * 产生随机的登入token
     *
     * @return
     */
    public String generateLoginToken() {
        return Constant.RANDOM.ACCESS_TOKEN_PREFIX + TimeUtil.getNewTime() + RandomUtil.getRandomNumbers(6);
    }
}
