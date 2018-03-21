package com.php25.distributedtransaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.distributedtransaction.config.RabbitmqConfigProperties;
import com.php25.distributedtransaction.constant.DistributedTransactionMsgStatus;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.php25.distributedtransaction.dto.MessageTypeDto;
import com.php25.distributedtransaction.model.DistributedTransactionMsgLog;
import com.php25.distributedtransaction.repository.DistributedTransactionMsgLogRepository;
import com.php25.distributedtransaction.service.DistributedTransactionMsgService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "distributed.transaction", value = "enabled", havingValue = "true")
public class DistributedTransactionMsgServiceImpl extends BaseServiceImpl<DistributedTransactionMsgLogDto, DistributedTransactionMsgLog> implements DistributedTransactionMsgService {

    private DistributedTransactionMsgLogRepository distributedTransactionMsgLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitmqConfigProperties rabbitmqConfigProperties;

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
                channel.basicPublish(rabbitmqConfigProperties.getExchange(), a.getQueueName(), new AMQP.BasicProperties.Builder().deliveryMode(2).build(), message.getBytes());
                if (channel.waitForConfirms(5000)) {
                    //更改状态
                    a.setStatus(DistributedTransactionMsgStatus.生产者已发送);
                    this.save(a);
                }
            } catch (Exception e) {
                Logger.getLogger(DistributedTransactionMsgServiceImpl.class).error(e);
            }
        });

        try {
            channel.close();
            connection.close();
            return true;
        } catch (Exception e) {
            Logger.getLogger(DistributedTransactionMsgServiceImpl.class).error(e);
            return true;
        }
    }
}
