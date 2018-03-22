package com.php25.distributedtransaction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by penghuiping on 2018/3/21.
 */
@Component
@ConfigurationProperties("distributed.transaction.rabbitmq")
public class RabbitmqConfigProperties {

    private String exchange;

    private List<String> queues;

    private String targetQueue;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public List<String> getQueues() {
        return queues;
    }

    public void setQueues(List<String> queues) {
        this.queues = queues;
    }

    public String getTargetQueue() {
        return targetQueue;
    }

    public void setTargetQueue(String targetQueue) {
        this.targetQueue = targetQueue;
    }
}
