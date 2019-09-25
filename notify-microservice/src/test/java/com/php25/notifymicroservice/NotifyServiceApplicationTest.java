package com.php25.notifymicroservice;


import com.php25.notifymicroservice.server.MailServiceTest;
import com.php25.notifymicroservice.server.MobileMessageServiceTest;
import com.php25.notifymicroservice.server.NotifyserviceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "development")
@SpringBootTest(classes = NotifyserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NotifyServiceApplicationTest {

    @Autowired
    public WebTestClient webTestClient;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private ReactiveWebApplicationContext context;

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
