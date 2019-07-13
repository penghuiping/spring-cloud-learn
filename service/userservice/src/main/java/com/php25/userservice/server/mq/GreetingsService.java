package com.php25.userservice.server.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * @author: penghuiping
 * @date: 2019/7/12 09:50
 * @description:
 */
@Service
@Slf4j
public class GreetingsService {
    private final GreetingsStreams greetingsStreams;

    public GreetingsService(GreetingsStreams greetingsStreams) {
        this.greetingsStreams = greetingsStreams;
    }

    public void sendGreeting() {
        MessageChannel messageChannel = greetingsStreams.outboundGreetings();
        messageChannel.send(MessageBuilder
                .withPayload("hello world")
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
