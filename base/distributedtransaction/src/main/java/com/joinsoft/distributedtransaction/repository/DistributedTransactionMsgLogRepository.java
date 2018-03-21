package com.joinsoft.distributedtransaction.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.distributedtransaction.model.DistributedTransactionMsgLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by penghuiping on 2017/9/20.
 */
@Repository
public interface DistributedTransactionMsgLogRepository extends BaseRepository<DistributedTransactionMsgLog, String> {

    @Query("from DistributedTransactionMsgLog a where a.status =:status")
    public List<DistributedTransactionMsgLog> findByStatus(@Param("status") Integer status);

}
