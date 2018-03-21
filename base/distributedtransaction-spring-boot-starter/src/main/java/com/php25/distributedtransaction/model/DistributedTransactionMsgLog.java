package com.php25.distributedtransaction.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by penghuiping on 2017/9/20.
 */
@Entity
@Table(name = "distributed_transaction_msg_log")
public class DistributedTransactionMsgLog {
    @Id
    private String id;

    @Column(name = "queue_name")
    private String queueName;

    private String body;

    @Column(name = "message_type")
    private String messageType;

    private Integer status;//0:生产者新消息 1:生产者已发送 2:消费者已处理

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
