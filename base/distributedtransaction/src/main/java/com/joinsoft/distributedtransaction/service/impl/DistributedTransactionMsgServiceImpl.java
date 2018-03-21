package com.joinsoft.distributedtransaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.joinsoft.common.service.impl.BaseServiceImpl;
import com.joinsoft.distributedtransaction.constant.Constant;
import com.joinsoft.distributedtransaction.constant.DistributedTransactionMsgStatus;
import com.joinsoft.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.joinsoft.distributedtransaction.dto.MessageTypeDto;
import com.joinsoft.distributedtransaction.model.DistributedTransactionMsgLog;
import com.joinsoft.distributedtransaction.repository.DistributedTransactionMsgLogRepository;
import com.joinsoft.distributedtransaction.service.DistributedTransactionMsgService;
import com.joinsoft.distributedtransaction.task.PublishMessageTask;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 2017/9/20.
 */
@Transactional
@Component(value = "distributedTransactionMsgService")
@ConditionalOnClass(name = "com.alibaba.druid.pool.DruidDataSource")
public class DistributedTransactionMsgServiceImpl extends BaseServiceImpl<DistributedTransactionMsgLogDto, DistributedTransactionMsgLog> implements DistributedTransactionMsgService, InitializingBean {

    private DistributedTransactionMsgLogRepository distributedTransactionMsgLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        //初始化所有的exchange,queue
        channel.exchangeDeclare(Constant.Exchange.BABY_SHOP_NAME, "direct", true);
        channel.queueDeclare(Constant.Queue.MERCHANT_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.COMMISSION_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.VIP_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.ACTIVITY_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.ORDER_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.OTHER_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.PAY_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.PRODUCT_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.RETURN_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.SERVICE_SERVICE, true, false, false, null);
        channel.queueDeclare(Constant.Queue.USER_SERVICE, true, false, false, null);
        channel.queueBind(Constant.Queue.MERCHANT_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.MERCHANT_SERVICE);
        channel.queueBind(Constant.Queue.COMMISSION_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.COMMISSION_SERVICE);
        channel.queueBind(Constant.Queue.VIP_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.VIP_SERVICE);
        channel.queueBind(Constant.Queue.ACTIVITY_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.ACTIVITY_SERVICE);
        channel.queueBind(Constant.Queue.ORDER_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.ORDER_SERVICE);
        channel.queueBind(Constant.Queue.OTHER_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.OTHER_SERVICE);
        channel.queueBind(Constant.Queue.PAY_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.PAY_SERVICE);
        channel.queueBind(Constant.Queue.PRODUCT_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.PRODUCT_SERVICE);
        channel.queueBind(Constant.Queue.RETURN_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.RETURN_SERVICE);
        channel.queueBind(Constant.Queue.SERVICE_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.SERVICE_SERVICE);
        channel.queueBind(Constant.Queue.USER_SERVICE, Constant.Exchange.BABY_SHOP_NAME, Constant.Queue.USER_SERVICE);

        channel.close();
        connection.close();
    }


    @Autowired
    public void setDistributedTransactionMsgLogRepository(DistributedTransactionMsgLogRepository distributedTransactionMsgLogRepository) {
        this.distributedTransactionMsgLogRepository = distributedTransactionMsgLogRepository;
        this.baseRepository = distributedTransactionMsgLogRepository;
    }

    public List<DistributedTransactionMsgLogDto> findByStatus(Integer status) {
        List<DistributedTransactionMsgLog> result0 = distributedTransactionMsgLogRepository.findByStatus(status);
        List<DistributedTransactionMsgLogDto> result1 = result0.stream().map(a -> {
            DistributedTransactionMsgLogDto temp = new DistributedTransactionMsgLogDto();
            BeanUtils.copyProperties(a, temp);
            temp.setStatus(DistributedTransactionMsgStatus.values()[a.getStatus()]);
            String messageTypeStr = a.getMessageType();
            MessageTypeDto messageTypeDto;
            try {
                messageTypeDto = objectMapper.readValue(messageTypeStr, MessageTypeDto.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            temp.setMessageType(messageTypeDto);
            return temp;
        }).collect(Collectors.toList());
        return result1;
    }

    @Override
    public DistributedTransactionMsgLogDto findOne(String id) {
        DistributedTransactionMsgLog a = distributedTransactionMsgLogRepository.findOne(id);
        DistributedTransactionMsgLogDto temp = new DistributedTransactionMsgLogDto();
        BeanUtils.copyProperties(a, temp);
        temp.setStatus(DistributedTransactionMsgStatus.values()[a.getStatus()]);
        String messageTypeStr = a.getMessageType();
        MessageTypeDto messageTypeDto;
        try {
            messageTypeDto = objectMapper.readValue(messageTypeStr, MessageTypeDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        temp.setMessageType(messageTypeDto);
        return temp;
    }

    @Override
    public DistributedTransactionMsgLogDto save(DistributedTransactionMsgLogDto obj) {
        DistributedTransactionMsgLog temp = new DistributedTransactionMsgLog();
        BeanUtils.copyProperties(obj, temp);
        temp.setStatus(obj.getStatus().value);
        MessageTypeDto messageTypeDto = obj.getMessageType();
        try {
            temp.setMessageType(objectMapper.writeValueAsString(messageTypeDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        temp = distributedTransactionMsgLogRepository.save(temp);
        obj.setId(temp.getId());
        return obj;
    }

    @Override
    public void save(Iterable<DistributedTransactionMsgLogDto> objs) {
        List<DistributedTransactionMsgLog> distributedTransactionMsgLogs = Lists.newArrayList(objs).stream().map(obj -> {
            DistributedTransactionMsgLog temp = new DistributedTransactionMsgLog();
            BeanUtils.copyProperties(obj, temp);
            temp.setStatus(obj.getStatus().value);
            MessageTypeDto messageTypeDto = obj.getMessageType();
            try {
                temp.setMessageType(objectMapper.writeValueAsString(messageTypeDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            temp = distributedTransactionMsgLogRepository.save(temp);
            return temp;
        }).collect(Collectors.toList());
        distributedTransactionMsgLogRepository.save(distributedTransactionMsgLogs);
    }

    public Boolean sendMsgToBroker(List<DistributedTransactionMsgLogDto> msgs) {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        msgs.forEach(a -> {
            try {
                channel.confirmSelect();
                String message = objectMapper.writeValueAsString(a);
                channel.basicPublish(Constant.Exchange.BABY_SHOP_NAME, a.getQueueName(), new AMQP.BasicProperties.Builder().deliveryMode(2).build(), message.getBytes());
                if (channel.waitForConfirms(5000)) {
                    //更改状态
                    a.setStatus(DistributedTransactionMsgStatus.生产者已发送);
                    this.save(a);
                }
            } catch (Exception e) {
                Logger.getLogger(PublishMessageTask.class).error(e);
            }
        });

        try {
            channel.close();
            connection.close();
            return true;
        } catch (Exception e) {
            Logger.getLogger(PublishMessageTask.class).error(e);
            return true;
        }
    }
}
