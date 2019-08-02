package com.php25.mediamicroservice.server.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author: penghuiping
 * @date: 2019/8/2 11:14
 * @description:
 */
@Log4j2
@Component
public class EventHandler {


    @StreamListener(Processor.INPUT)
    public void handler(Message message) {
        log.info("message:{}", message.getPayload().toString());
    }
}
