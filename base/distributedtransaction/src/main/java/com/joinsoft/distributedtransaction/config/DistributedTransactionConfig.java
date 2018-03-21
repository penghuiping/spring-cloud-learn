package com.joinsoft.distributedtransaction.config;

import com.joinsoft.common.repository.impl.BaseRepositoryImpl;
import com.joinsoft.distributedtransaction.repository.DistributedTransactionMsgLogRepository;
import com.joinsoft.distributedtransaction.repository.impl.DistributedTransactionMsgLogRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * Created by penghuiping on 2017/9/20.
 */
@Configuration
public class DistributedTransactionConfig {

    @Bean
    @ConditionalOnClass(name = "com.alibaba.druid.pool.DruidDataSource")
    public DistributedTransactionMsgLogRepository distributedTransactionMsgLogRepository(@Autowired EntityManager entityManager) {
        RepositoryFactorySupport jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
        jpaRepositoryFactory.setRepositoryBaseClass(BaseRepositoryImpl.class);
        DistributedTransactionMsgLogRepository distributedTransactionMsgLogRepository = jpaRepositoryFactory.getRepository(DistributedTransactionMsgLogRepository.class, DistributedTransactionMsgLogRepositoryImpl.class);
        return distributedTransactionMsgLogRepository;
    }




}
