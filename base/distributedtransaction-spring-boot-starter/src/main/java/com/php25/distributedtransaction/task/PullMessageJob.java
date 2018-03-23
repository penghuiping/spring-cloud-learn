package com.php25.distributedtransaction.task;

import com.php25.distributedtransaction.config.RabbitmqConfigProperties;
import com.php25.distributedtransaction.jms.DistributedTransactionJms;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by penghuiping on 2018/3/21.
 */
public class PullMessageJob implements org.quartz.Job {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqConfigProperties rabbitmqConfigProperties;

    @Autowired
    private DistributedTransactionJms distributedTransactionJms;


    public PullMessageJob() {

    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Connection connection1 = null;
        Channel channel1 = null;
        try {
            Logger.getLogger(DistributedTransactionJms.class).debug("每4秒处理拉取一次消息队列中的消息进行处理");
            ConnectionFactory connectionFactory = this.rabbitTemplate.getConnectionFactory();
            connection1 = connectionFactory.createConnection();
            channel1 = connection1.createChannel(false);
            boolean autoAck = false;
            while (true) {
                GetResponse getResponse = channel1.basicGet(rabbitmqConfigProperties.getTargetQueue(), autoAck);
                if (null != getResponse) {
                    distributedTransactionJms.handleMsg(channel1, getResponse.getEnvelope(), getResponse.getBody());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(PullMessageJob.class).error("", e);
        } finally {
            try {
                channel1.close();
            } catch (IOException e) {
                Logger.getLogger(PullMessageJob.class).error("", e);
            } catch (TimeoutException e) {
                Logger.getLogger(PullMessageJob.class).error("", e);
            }
            connection1.close();
        }

    }
}
