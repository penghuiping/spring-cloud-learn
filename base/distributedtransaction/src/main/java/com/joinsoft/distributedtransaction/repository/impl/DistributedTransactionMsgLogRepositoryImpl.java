package com.joinsoft.distributedtransaction.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by penghuiping on 2017/9/20.
 */
public class DistributedTransactionMsgLogRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;
}
