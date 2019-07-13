package com.php25.usermicroservice.server.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author: penghuiping
 * @date: 2019/7/11 21:42
 * @description:
 */
@Component
public interface SinkSender {

    String OUTPUT = "input";

    @Output(SinkSender.OUTPUT)
    MessageChannel output();

}
