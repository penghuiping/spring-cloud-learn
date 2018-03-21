## 使用说明
1. 基于spring cloud框架
2. 微服务框架上需要设置使用@EnableFeignClients

## 配置maven依赖

```
<dependency>
    <artifactId>distributedtransaction</artifactId>
    <groupId>com.joinsoft</groupId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```


## 异步消息实现最终一致性方案

### 创建数据表，每个微服务如果是不同数据库，那么每个数据库都需要对应着一张表。

mysql 数据库
```
create table distributed_transaction_msg_log
(
	id varchar(32) not null
		primary key,
	queue_name varchar(50) not null,
	body text not null,
	status int not null,
	message_type varchar(255) null
)
;

comment on table distributed_transaction_msg_log is '分布式事务日志表'
;

comment on column distributed_transaction_msg_log.id is '主键'
;

comment on column distributed_transaction_msg_log.queue_name is '队列名字'
;

comment on column distributed_transaction_msg_log.body is '消息体'
;

comment on column distributed_transaction_msg_log.status is '消息状态'
;

comment on column distributed_transaction_msg_log.message_type is '消息类型'
;


```

### 配置DistributedTransactionJmsbean

```
@Bean
@ConditionalOnClass(name = "com.alibaba.druid.pool.DruidDataSource")
public DistributedTransactionJms distributedTransactionJms(@Autowired RabbitTemplate rabbitTemplate,
                                                           @Autowired ObjectMapper objectMapper,
                                                           @Autowired DistributedTransactionMsgService distributedTransactionMsgService,
                                                           @Autowired TransactionTemplate transactionTemplate,
                                                           @Autowired ApplicationContext applicationContext) {
    String queueName = Constant.Queue.USER_SERVICE;
    return new DistributedTransactionJms(rabbitTemplate, objectMapper, distributedTransactionMsgService, transactionTemplate, applicationContext, queueName);
}
```

### 使用方法

假如有一个注册新用户的需要，用户信息与用户额外信息与佣金信息，分别对应userservice，vipservice与commissionservice。这个注册方法就需要实现保存用户信息以后，继续把vip信息与commission信息也给保存了

那么可以通过下面方式实现

```apple js

//构建DistributedTransactionMsgLogDto，新建vip中的用户额外信息
DistributedTransactionMsgLogDto logDto0 = new DistributedTransactionMsgLogDto();
logDto0.setId(idGeneratorService.getModelPrimaryKey());
logDto0.setStatus(DistributedTransactionMsgStatus.生产者新消息);
try {
    //告诉分布式事务模块自动消息处理器，需要调用的方法的参数值
    logDto0.setBody(objectMapper.writeValueAsString(customerExtraDto));
} catch (JsonProcessingException e) {
    throw new JsonException(e);
}
logDto0.setQueueName(Constant.Queue.VIP_SERVICE);
//告诉分布式事务模块自动消息处理器，需要调用的类的类名，方法名与参数类型
MessageTypeDto messageTypeDto0 = new MessageTypeDto();
messageTypeDto0.setClassName(CustomerExtraService.class.getName());
messageTypeDto0.setClassMethod("save");

TypeReferenceDto typeReferenceDto0 = new TypeReferenceDto();
typeReferenceDto0.setClassName(CustomerExtraDto.class.getName());
messageTypeDto0.setClassMethodParams(Lists.newArrayList(typeReferenceDto0));
logDto0.setMessageType(messageTypeDto0);

//佣金信息省略

//新建用户信息，并且通过异步消息实现新建vip信息与佣金信息，分别对应vip微服务与佣金微服务，user信息对应user微服务
//先落地用户信息表和vip信息与佣金信息的消息表
List<DistributedTransactionMsgLogDto> distributedTransactionMsgLogDtos = Lists.newArrayList(logDto0,logDto1);
AtomicReference<CustomerDto> customerDtoReference = new AtomicReference<>();

Boolean result = transactionTemplate.execute((TransactionStatus a) -> {
    CustomerDto temp = customerService.save(customerDto);
    customerDtoReference.set(temp);
    distributedTransactionMsgService.save(distributedTransactionMsgLogDtos);
    return true;
});

//异步发送消息
Observable.create(subscriber -> {
    distributedTransactionMsgService.sendMsgToBroker(distributedTransactionMsgLogDtos);
}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(a->{

});
```
