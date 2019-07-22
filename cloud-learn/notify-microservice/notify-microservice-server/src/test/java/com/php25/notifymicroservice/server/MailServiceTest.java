package com.php25.notifymicroservice.server;

import com.google.common.collect.Lists;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.server.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by penghuiping on 2018/5/1.
 */
@SpringBootTest
@ContextConfiguration(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class MailServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceTest.class);

    @Autowired
    MailService mailService;

    @Test
    public void sendSimpleMail() throws Exception {
        mailService.sendSimpleMail("575813261@qq.com", "测试", "这是一个测试");
    }

    @Test
    public void sendAttachmentsMail() throws Exception {
        Pair<String, File> pairDto = new Pair<>();
        pairDto.setKey("图片");
        pairDto.setValue(new File("/Users/penghuiping/Desktop/1.png"));
        mailService.sendAttachmentsMail("575813261@qq.com", "测试", "这是一个测试", Lists.newArrayList(pairDto));
    }

    @Test
    public void sendTemplateMail() throws Exception {
        Pair<String, File> pairDto = new Pair<>();
        pairDto.setKey("图片");
        pairDto.setValue(new File("/Users/penghuiping/Desktop/1.png"));

        Map<String, Object> map = new HashMap<>();
        map.put("username", "peng");
        mailService.sendTemplateMail("575813261@qq.com", "测试", map, Lists.newArrayList(pairDto));
    }


}
