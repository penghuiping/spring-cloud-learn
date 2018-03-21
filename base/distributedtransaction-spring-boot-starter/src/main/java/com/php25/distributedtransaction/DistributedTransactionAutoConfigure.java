package com.php25.distributedtransaction;

import com.php25.common.repository.impl.BaseRepositoryImpl;
import com.php25.distributedtransaction.config.RabbitmqConfigProperties;
import com.php25.distributedtransaction.task.PullMessageJob;
import com.rabbitmq.client.Channel;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */
@ComponentScan
@Configuration
@ConditionalOnProperty(prefix = "distributed.transaction", value = "enabled", havingValue = "true")
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class DistributedTransactionAutoConfigure implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqConfigProperties rabbitmqConfigProperties;


    @Override
    public void afterPropertiesSet() throws Exception {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        //初始化所有的exchange,queue
        String exchange = rabbitmqConfigProperties.getExchange();
        List<String> queues = rabbitmqConfigProperties.getQueues();
        String targetQueue = rabbitmqConfigProperties.getTargetQueue();

        channel.exchangeDeclare(exchange, "direct", true);

        for (String queueName : queues) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchange, queueName);
        }

        channel.close();
        connection.close();


        // Grab the Scheduler instance from the Factory
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // and start it off
        scheduler.start();

        // define the job and tie it to our MyJob class
        JobDetail job = JobBuilder.newJob(PullMessageJob.class)
                .withIdentity("job1", "group1")
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
    }


}
