package com.php25.distributedtransaction.task;

import com.php25.distributedtransaction.constant.DistributedTransactionMsgStatus;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.php25.distributedtransaction.service.DistributedTransactionMsgService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by penghuiping on 2018/3/21.
 */
public class PushMessageJob implements org.quartz.Job {

    @Autowired
    private DistributedTransactionMsgService distributedTransactionMsgService;


    public PushMessageJob() {

    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logger.getLogger(PushMessageJob.class).debug("每隔4秒扫一次分布式事务消息表，发送新消息到消息队列");
        List<DistributedTransactionMsgLogDto> msgs = distributedTransactionMsgService.findByStatus(DistributedTransactionMsgStatus.生产者新消息.value);
        distributedTransactionMsgService.sendMsgToBroker(msgs);
    }
}
