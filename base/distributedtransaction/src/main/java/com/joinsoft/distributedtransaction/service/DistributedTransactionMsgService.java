package com.joinsoft.distributedtransaction.service;

import com.joinsoft.common.service.BaseService;
import com.joinsoft.common.service.SoftDeletable;
import com.joinsoft.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.joinsoft.distributedtransaction.model.DistributedTransactionMsgLog;

import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */
public interface DistributedTransactionMsgService extends BaseService<DistributedTransactionMsgLogDto, DistributedTransactionMsgLog>, SoftDeletable<DistributedTransactionMsgLogDto> {

    public List<DistributedTransactionMsgLogDto> findByStatus(Integer status);

    /**
     * 发送消息至消息队列
     *
     * @param msgs
     * @return
     */
    public Boolean sendMsgToBroker(List<DistributedTransactionMsgLogDto> msgs);
}
