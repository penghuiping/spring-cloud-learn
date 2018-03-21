package com.php25.distributedtransaction.dto;

import com.php25.distributedtransaction.constant.DistributedTransactionMsgStatus;

import java.io.Serializable;

/**
 * Created by penghuiping on 2017/9/20.
 */
public class DistributedTransactionMsgLogDto implements Serializable {
    private String id;

    private String body;//json字符串

    private String queueName;

    private DistributedTransactionMsgStatus status;//0:生产者新消息 1:生产者已发送 2:消费者已处理

    private MessageTypeDto messageType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DistributedTransactionMsgStatus getStatus() {
        return status;
    }

    public void setStatus(DistributedTransactionMsgStatus status) {
        this.status = status;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public MessageTypeDto getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeDto messageType) {
        this.messageType = messageType;
    }
}
