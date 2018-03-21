package com.joinsoft.distributedtransaction.task;

import com.joinsoft.distributedtransaction.constant.DistributedTransactionMsgStatus;
import com.joinsoft.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.joinsoft.distributedtransaction.service.DistributedTransactionMsgService;
import org.apache.log4j.Logger;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */
@Component
@ConditionalOnClass(name = "com.alibaba.druid.pool.DruidDataSource")
public class PublishMessageTask {

    @Autowired
    private DistributedTransactionMsgService distributedTransactionMsgService;

    /**
     * 每隔8秒扫一次分布式事务消息表，发送新消息到消息队列
     */
    @Scheduled(cron = "0/4 * * * * ?")
    public void sendMessageToBroker() {
        Logger.getLogger(PublishMessageTask.class).info("每隔4秒扫一次分布式事务消息表，发送新消息到消息队列");
        List<DistributedTransactionMsgLogDto> msgs = distributedTransactionMsgService.findByStatus(DistributedTransactionMsgStatus.生产者新消息.value);
        distributedTransactionMsgService.sendMsgToBroker(msgs);
    }

    //todo 定期删除消费者已处理的消息
}
