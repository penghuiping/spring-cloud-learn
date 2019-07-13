package com.php25.usermicroservice.server.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: penghuiping
 * @date: 2019/7/11 21:43
 * @description:
 */
@RunWith(SpringRunner.class)
@EnableBinding(value = {SinkSender.class})
public class MsgTest {

    @Autowired
    private SinkSender sinkSender;

    @org.junit.Test
    public void sendMessage(){
        sinkSender.output().send(MessageBuilder.withPayload("啥").build());
    }
}
