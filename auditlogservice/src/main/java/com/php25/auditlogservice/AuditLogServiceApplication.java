package com.php25.auditlogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author penghuiping
 * @date 2019/12/3 15:49
 */
@ComponentScan(value = {"com.php25.auditlogservice.server", "com.php25.common.flux"})
@SpringBootApplication
@EnableTransactionManagement
@EnableBinding({Processor.class})
public class AuditLogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditLogServiceApplication.class, args);
    }
}
