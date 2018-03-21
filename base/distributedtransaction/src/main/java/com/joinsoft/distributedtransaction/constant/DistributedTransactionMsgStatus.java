package com.joinsoft.distributedtransaction.constant;

/**
 * Created by penghuiping on 2017/9/20.
 */
public enum DistributedTransactionMsgStatus {

    生产者新消息(0),
    生产者已发送(1),
    消费者已处理(2);

    public int value;

    DistributedTransactionMsgStatus(int value) {
        this.value = value;
    }

    public String getName() {
        return this.name();
    }

}
