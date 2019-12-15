package com.php25.auditlogservice.server.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author: penghuiping
 * @date: 2019/8/2 11:14
 * @description:
 */
@Slf4j
@Component
public class EventHandler {

    @Autowired
    private MongoTemplate mongoTemplate;

    @StreamListener(value = Processor.INPUT,condition = "headers['type']=='auditlogservice'")
    public void handler(Message message) {
        log.info("message:{}", message.getPayload().toString());
    }
}
