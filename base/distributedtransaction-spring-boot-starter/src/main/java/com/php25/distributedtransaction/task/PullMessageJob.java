package com.php25.distributedtransaction.task;

import com.php25.distributedtransaction.jms.DistributedTransactionJms;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * Created by penghuiping on 2018/3/21.
 */
public class PullMessageJob implements org.quartz.Job {

    public PullMessageJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
//        Logger.getLogger(DistributedTransactionJms.class).info("每4秒处理拉取一次消息队列中的消息进行处理");
//        ConnectionFactory connectionFactory = this.rabbitTemplate.getConnectionFactory();
//        Connection connection1 = connectionFactory.createConnection();
//        Channel channel1 = connection1.createChannel(false);
//        boolean autoAck = false;
//        while (true) {
//            GetResponse getResponse = channel1.basicGet(this.queueName, autoAck);
//            if (null != getResponse) {
//                handleMsg(channel1, getResponse.getEnvelope(), getResponse.getBody());
//            } else {
//                break;
//            }
//        }
//        channel1.close();
//        connection1.close();
    }
}
