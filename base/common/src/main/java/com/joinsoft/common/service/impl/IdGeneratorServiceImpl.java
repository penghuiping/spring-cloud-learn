package com.joinsoft.common.service.impl;


import com.joinsoft.common.service.IdGeneratorService;
import com.joinsoft.common.util.RandomUtil;
import com.joinsoft.common.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by penghuiping on 2017/9/18.
 */
@Service("idGeneratorService")
public class IdGeneratorServiceImpl implements IdGeneratorService {

    @Override
    public String getGoodsOrderNumber() {
        return TimeUtil.getNewTime() + RandomUtil.getRandomNumbers(14);
    }

    @Override
    public String getTicketsNumber() {
        return TimeUtil.getNewTime() + RandomUtil.getRandomNumbers(14);
    }

    @Override
    public String getActivityOrderNumber() {
        return TimeUtil.getNewTime() + RandomUtil.getRandomNumbers(14);
    }

    @Override
    public String getVipOrderNumber() {
        return TimeUtil.getNewTime() + RandomUtil.getRandomNumbers(14);
    }

    @Override
    public String getModelPrimaryKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
