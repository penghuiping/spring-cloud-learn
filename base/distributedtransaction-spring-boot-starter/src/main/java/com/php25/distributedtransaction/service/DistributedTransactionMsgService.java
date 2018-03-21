package com.php25.distributedtransaction.service;

import com.php25.common.service.BaseService;
import com.php25.common.service.SoftDeletable;
import com.php25.distributedtransaction.dto.DistributedTransactionMsgLogDto;
import com.php25.distributedtransaction.model.DistributedTransactionMsgLog;

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
