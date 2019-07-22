package com.php25.usermicroservice.server.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author: penghuiping
 * @date: 2019/7/12 09:53
 * @description:
 */
@Component
@Slf4j
public class GreetingsListener {

    @StreamListener(GreetingsStreams.INPUT)
    public void handleGreetings(@Payload String greetings) {
        log.info("Received greetings: {}", greetings);
    }
}
