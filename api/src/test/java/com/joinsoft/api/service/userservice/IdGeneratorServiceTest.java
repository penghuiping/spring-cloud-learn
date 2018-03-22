package com.joinsoft.api.service.userservice;

import com.joinsoft.api.BaseTest;
import com.joinsoft.userservice.server.service.IdGeneratorService;
import org.junit.Test;
import org.redisson.api.GeoEntry;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by penghuiping on 2018/2/23.
 */
public class IdGeneratorServiceTest extends BaseTest {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() {
        //System.out.println(idGeneratorService.generateLoginToken());

        RGeo geo = redissonClient.getGeo("test");
        geo.addAsync(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));
//        geo.add(13.361389, 38.115556, "Palermo");
//        Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS);
//        System.out.println(distance);
//        geo.readAll().stream().forEach(a->{
//            System.out.println(a.toString());
//        });
    }
}
