package com.php25.distributedtransaction.jms;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.util.StringUtil;
import com.php25.distributedtransaction.config.RabbitmqConfigProperties;
import com.php25.distributedtransaction.constant.DistributedTransactionMsgStatus;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.php25.distributedtransaction.dto.MessageTypeDto;
import com.php25.distributedtransaction.dto.TypeReferenceDto;
import com.php25.distributedtransaction.service.DistributedTransactionMsgService;
import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */

@Component
public class DistributedTransactionJms {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Connection connection;

    @Autowired
    private DistributedTransactionMsgService distributedTransactionMsgService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RabbitmqConfigProperties rabbitmqConfigProperties;


    /**
     * 使用服务器推送长连接模式
     */
    private void commonHandler() {
        ConnectionFactory connectionFactory = this.rabbitTemplate.getConnectionFactory();
        connection = connectionFactory.createConnection();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                Logger.getLogger(DistributedTransactionJms.class).info("关闭rabbitmq连接");
                if (null != connection) {
                    connection.close();
                }
            }
        });
        Channel channel = connection.createChannel(false);
        try {
            boolean autoAck = false;
            channel.basicConsume(rabbitmqConfigProperties.getTargetQueue(), autoAck,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            handleMsg(channel, envelope, body);
                        }
                    });
        } catch (Exception e) {
            if (null != channel) try {
                channel.close();
            } catch (Exception e1) {
                Logger.getLogger(DistributedTransactionJms.class).error(e1);
            }
            Logger.getLogger(DistributedTransactionJms.class).error(e);
        }
    }


    /**
     * 客户端主动拉取模式
     * 每4秒处理拉取一次消息队列中的消息进行处理
     *
     * @throws Exception
     */
    @Scheduled(cron = "0/4 * * * * ?")
    private void scheduledPullMessage() throws Exception {
        Logger.getLogger(DistributedTransactionJms.class).info("每4秒处理拉取一次消息队列中的消息进行处理");
        ConnectionFactory connectionFactory = this.rabbitTemplate.getConnectionFactory();
        Connection connection1 = connectionFactory.createConnection();
        Channel channel1 = connection1.createChannel(false);
        boolean autoAck = false;
        while (true) {
            GetResponse getResponse = channel1.basicGet(rabbitmqConfigProperties.getTargetQueue(), autoAck);
            if (null != getResponse) {
                handleMsg(channel1, getResponse.getEnvelope(), getResponse.getBody());
            } else {
                break;
            }
        }
        channel1.close();
        connection1.close();
    }

    /**
     * 不同service对应不同数据，使用此方法
     *
     * @param channel
     * @param envelope
     * @param body
     * @throws IOException
     */
    private void handleMsgDistribute(Channel channel, Envelope envelope, byte[] body) throws IOException {
        //判断消息头信息中的消息类型，分发消息处理
        //为了降低复杂度,目前只支持单参数方法
        // 处理消息
        DistributedTransactionMsgLogDto distributedTransactionMsgLogDto = objectMapper.readValue(body, DistributedTransactionMsgLogDto.class);
        MessageTypeDto messageTypeDto = distributedTransactionMsgLogDto.getMessageType();
        TypeReferenceDto methodParamName = messageTypeDto.getClassMethodParam();

        Boolean result = transactionTemplate.execute(a -> {
            //实现幂等性，意思是多次调用次方法与调用一次的效果是一致的，不会出现数据的重复或其他什么。

            //判断是否已经处理过
            DistributedTransactionMsgLogDto temp = distributedTransactionMsgService.findOne(distributedTransactionMsgLogDto.getId());
            if (null != temp && DistributedTransactionMsgStatus.消费者已处理.equals(temp.getStatus())) {
                //以前已经处理过此消息，不需要再次处理直接返回
                return true;
            } else {
                //处理此消息
                try {
                    Object obj = context.getBean(Class.forName(messageTypeDto.getClassName()));
                    TypeReferenceDto typeReferenceDto = methodParamName;
                    Class methodParamCls = Class.forName(typeReferenceDto.getClassName());
                    Method method;
                    Object methodParam = null;//方法参数对象
                    if (typeReferenceDto.getClassName().equals(Iterable.class.getName())) {
                        //构建JavaType
                        JavaType javaType;
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        if (!StringUtil.isBlank(typeReferenceDto.getGenericTypeName())) {
                            //List具有泛型的情况
                            Class paramGenericTypeCls = Class.forName(typeReferenceDto.getGenericTypeName());
                            javaType = objectMapper.getTypeFactory().constructParametricType(paramCls, paramGenericTypeCls);
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), javaType);
                        } else {
                            //List没有泛型的情况
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        }
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), Iterable.class);
                    } else if (typeReferenceDto.getClassName().equals(List.class.getName())) {
                        //构建JavaType
                        JavaType javaType;
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        if (!StringUtil.isBlank(typeReferenceDto.getGenericTypeName())) {
                            //List具有泛型的情况
                            Class paramGenericTypeCls = Class.forName(typeReferenceDto.getGenericTypeName());
                            javaType = objectMapper.getTypeFactory().constructParametricType(paramCls, paramGenericTypeCls);
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), javaType);
                        } else {
                            //List没有泛型的情况
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        }
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), List.class);
                    } else {
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), Object.class);
                    }
                    method.invoke(obj, methodParam);
                    distributedTransactionMsgLogDto.setStatus(DistributedTransactionMsgStatus.消费者已处理);
                    distributedTransactionMsgService.save(distributedTransactionMsgLogDto);
                    return true;
                } catch (Exception e) {
                    Logger.getLogger(DistributedTransactionJms.class).error(e);
                    throw new RuntimeException(e);
                }
            }
        });

        if (true == result) {
            //成功处理,consumer回复ack
            channel.basicAck(envelope.getDeliveryTag(), false);
        } else
            channel.basicNack(envelope.getDeliveryTag(), false, true);

    }

    /**
     * 只用一个数据库用此方法
     *
     * @param channel
     * @param envelope
     * @param body
     * @throws IOException
     */
    private void handleMsg(Channel channel, Envelope envelope, byte[] body) throws IOException {
        //判断消息头信息中的消息类型，分发消息处理
        //为了降低复杂度,目前只支持单参数方法
        // 处理消息
        DistributedTransactionMsgLogDto distributedTransactionMsgLogDto = objectMapper.readValue(body, DistributedTransactionMsgLogDto.class);
        MessageTypeDto messageTypeDto = distributedTransactionMsgLogDto.getMessageType();
        TypeReferenceDto methodParamName = messageTypeDto.getClassMethodParam();

        Boolean result = transactionTemplate.execute(a -> {
            //实现幂等性，意思是多次调用次方法与调用一次的效果是一致的，不会出现数据的重复或其他什么。

            //判断是否已经处理过

            DistributedTransactionMsgLogDto temp = distributedTransactionMsgService.findOne(distributedTransactionMsgLogDto.getId());
            if (null != temp && DistributedTransactionMsgStatus.消费者已处理.equals(temp.getStatus())) {
                //以前已经处理过此消息，不需要再次处理直接返回
                return true;
            } else if (null != temp && DistributedTransactionMsgStatus.生产者已发送.equals(temp.getStatus())) {
                //处理此消息
                try {
                    Object obj = context.getBean(Class.forName(messageTypeDto.getClassName()));
                    TypeReferenceDto typeReferenceDto = methodParamName;
                    Class methodParamCls = Class.forName(typeReferenceDto.getClassName());
                    Method method;
                    Object methodParam = null;//方法参数对象
                    if (typeReferenceDto.getClassName().equals(Iterable.class.getName())) {
                        //构建JavaType
                        JavaType javaType;
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        if (!StringUtil.isBlank(typeReferenceDto.getGenericTypeName())) {
                            //List具有泛型的情况
                            Class paramGenericTypeCls = Class.forName(typeReferenceDto.getGenericTypeName());
                            javaType = objectMapper.getTypeFactory().constructParametricType(paramCls, paramGenericTypeCls);
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), javaType);
                        } else {
                            //List没有泛型的情况
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        }
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), Iterable.class);
                    } else if (typeReferenceDto.getClassName().equals(List.class.getName())) {
                        //构建JavaType
                        JavaType javaType;
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        if (!StringUtil.isBlank(typeReferenceDto.getGenericTypeName())) {
                            //List具有泛型的情况
                            Class paramGenericTypeCls = Class.forName(typeReferenceDto.getGenericTypeName());
                            javaType = objectMapper.getTypeFactory().constructParametricType(paramCls, paramGenericTypeCls);
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), javaType);
                        } else {
                            //List没有泛型的情况
                            methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        }
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), List.class);
                    } else {
                        Class paramCls = Class.forName(typeReferenceDto.getClassName());
                        methodParam = objectMapper.readValue(distributedTransactionMsgLogDto.getBody(), paramCls);
                        method = obj.getClass().getDeclaredMethod(messageTypeDto.getClassMethod(), methodParamCls);
                    }
                    method.invoke(obj, methodParam);
                    distributedTransactionMsgLogDto.setStatus(DistributedTransactionMsgStatus.消费者已处理);
                    distributedTransactionMsgService.save(distributedTransactionMsgLogDto);
                    return true;
                } catch (Exception e) {
                    Logger.getLogger(DistributedTransactionJms.class).error(e);
                    throw new RuntimeException(e);
                }
            } else {
                return false;
            }
        });

        if (true == result) {
            //成功处理,consumer回复ack
            channel.basicAck(envelope.getDeliveryTag(), false);
        } else
            channel.basicNack(envelope.getDeliveryTag(), false, true);
    }
}
