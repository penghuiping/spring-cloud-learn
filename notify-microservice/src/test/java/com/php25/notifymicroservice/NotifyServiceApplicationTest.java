package com.php25.notifymicroservice;


import com.php25.notifymicroservice.server.MailServiceTest;
import com.php25.notifymicroservice.server.MobileMessageServiceTest;
import com.php25.notifymicroservice.server.NotifyserviceApplication;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test-postgres")
@SpringBootTest(classes = NotifyserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NotifyServiceApplicationTest {

    @Autowired
    public WebTestClient webTestClient;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private ReactiveWebApplicationContext context;

    @ClassRule
    public static GenericContainer redis = new GenericContainer<>("redis:5.0.3-alpine")
            .withExposedPorts(6379);

    @ClassRule
    public static GenericContainer rabbitmq = new GenericContainer<>("rabbitmq:3.7.17-management-alpine")
            .withExposedPorts(5672)
            .withEnv("RABBITMQ_DEFAULT_USER", "admin")
            .withEnv("RABBITMQ_DEFAULT_PASS", "admin");

    @ClassRule
    public static GenericContainer postgres = new GenericContainer<>("postgres:12.0-alpine")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "admin")
            .withEnv("POSTGRES_PASSWORD", "admin")
            .withEnv("POSTGRES_DB", "test");

    static {
        redis.setPortBindings(Lists.newArrayList("6379:6379"));
        rabbitmq.setPortBindings(Lists.newArrayList("5672:5672"));
        postgres.setPortBindings(Lists.newArrayList("5432:5432"));
    }


    @Before
    public void setUp() {
        this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .filter(documentationConfiguration(this.restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }


    @Autowired
    private MailServiceTest mailServiceTest;

    @Autowired
    private MobileMessageServiceTest mobileMessageServiceTest;

    @Test
    public void test() throws Exception {
        mobileMessageServiceTest.sendSMS(this);
        mobileMessageServiceTest.validateSMS(this);
        mailServiceTest.sendSimpleMail(this);
        mailServiceTest.sendAttachmentsMail(this);
    }
}
