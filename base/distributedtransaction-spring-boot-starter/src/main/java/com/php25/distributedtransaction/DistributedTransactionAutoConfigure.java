package com.php25.distributedtransaction;

import com.php25.common.repository.impl.BaseRepositoryImpl;
import com.php25.distributedtransaction.config.RabbitmqConfigProperties;
import com.php25.distributedtransaction.config.SpringJobFactory;
import com.php25.distributedtransaction.jms.DistributedTransactionJms;
import com.php25.distributedtransaction.task.PullMessageJob;
import com.php25.distributedtransaction.task.PushMessageJob;
import com.rabbitmq.client.Channel;
import org.quartz.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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

    @Autowired
    private DistributedTransactionJms distributedTransactionJms;

    @Autowired
    private SpringJobFactory springJobFactory;

    @Autowired
    private Scheduler scheduler;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(springJobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

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


        //创建一个job
        JobDetail job1 = JobBuilder.newJob(PullMessageJob.class)
                .withIdentity("job1", "group1")
                .build();

        JobDetail job2 = JobBuilder.newJob(PushMessageJob.class)
                .withIdentity("job2", "group1")
                .build();

        //创建一个trigger
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(4)
                        .repeatForever())
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(4)
                        .repeatForever())
                .build();

        //绑定一个job与trigger
        scheduler.scheduleJob(job1, trigger1);
        scheduler.scheduleJob(job2, trigger2);
    }


}
