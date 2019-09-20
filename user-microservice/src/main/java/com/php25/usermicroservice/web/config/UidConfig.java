package com.php25.usermicroservice.web.config;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.baidu.fsg.uid.worker.dao.WorkerNodeDAO;
import com.baidu.fsg.uid.worker.dao.WorkerNodeDaoImpl;
import com.php25.common.db.Db;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author penghuiping
 * @date 2019/9/19 18:19
 */
@Configuration
public class UidConfig {

    @Bean
    UidGenerator uidGenerator(WorkerIdAssigner workerIdAssigner) {
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        defaultUidGenerator.setTimeBits(29);
        defaultUidGenerator.setWorkerBits(21);
        defaultUidGenerator.setSeqBits(13);
        defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner);
        defaultUidGenerator.setEpochStr("2019-08-01");
        return defaultUidGenerator;
    }

    @Bean
    DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    WorkerNodeDAO workerNodeDAO(Db db) {
        return new WorkerNodeDaoImpl(db);
    }
}
