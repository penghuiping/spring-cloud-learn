package com.joinsoft.api.service.userservice;

import com.joinsoft.api.BaseTest;
import com.joinsoft.userservice.service.IdGeneratorService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by penghuiping on 2018/2/23.
 */
public class IdGeneratorServiceTest extends BaseTest {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Test
    public void test() {
        System.out.println(idGeneratorService.generateLoginToken());
    }
}
