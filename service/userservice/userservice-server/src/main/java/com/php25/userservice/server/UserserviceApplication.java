package com.php25.userservice.server;


import com.php25.common.repository.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {"com.php25", "com.joinsoft"})
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class UserserviceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserserviceApplication.class);
        app.run(args);
    }
}