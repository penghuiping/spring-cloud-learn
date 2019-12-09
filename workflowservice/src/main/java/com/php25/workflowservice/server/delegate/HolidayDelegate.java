package com.php25.workflowservice.server.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author penghuiping
 * @date 2019/12/5 15:43
 */
@Slf4j
public class HolidayDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("do something...");
    }
}
