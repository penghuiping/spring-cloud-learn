package com.php25.mediamicroservice;

import com.google.common.collect.Maps;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.core.util.crypto.constant.RsaAlgorithm;
import com.php25.common.core.util.crypto.key.SecretKeyUtil;
import com.php25.mediamicroservice.server.ImageServiceTest;
import com.php25.mediamicroservice.server.MediaServiceApplication;
import com.php25.mediamicroservice.server.constant.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;

import java.security.PrivateKey;
import java.util.Date;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * @author penghuiping
 * @date 2019/10/8 14:29
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test-postgres")
@SpringBootTest(classes = MediaServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MediaServiceApplicationTest {

    @Autowired
    public WebTestClient webTestClient;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

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

    @ClassRule
    public static GenericContainer zipkin = new GenericContainer<>("openzipkin/zipkin")
            .withExposedPorts(9411);

    @ClassRule
    public static GenericContainer eureka = new GenericContainer<>("springcloud/eureka")
            .withExposedPorts(8761);

    static {
        redis.setPortBindings(Lists.newArrayList("6379:6379"));
        rabbitmq.setPortBindings(Lists.newArrayList("5672:5672"));
        postgres.setPortBindings(Lists.newArrayList("5432:5432"));
        zipkin.setPortBindings(Lists.newArrayList("9411:9411"));
        eureka.setPortBindings(Lists.newArrayList("8761:8761"));
    }


    @Autowired
    private ReactiveWebApplicationContext context;

    public String imageId;

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
    private ImageServiceTest imageServiceTest;


    public String jwt;

    @Test
    public void test() throws Exception {
        jwt = generateJwt();
        imageServiceTest.save(this);
        imageServiceTest.findOne(this);
    }

    @Autowired
    private NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder;

    @Test
    public void jwtTest() throws Exception{
        jwt = generateJwt();
        log.info("jwt:{}",jwt);
    }


    @Value("${jwt.privateKey}")
    private String privateKey;

    private String generateJwt() throws Exception {
        PrivateKey privateKey1 = SecretKeyUtil.generatePrivateKey(RsaAlgorithm.RSA.getValue(), DigestUtil.decodeBase64(privateKey));
        return Jwts.builder().signWith(privateKey1, SignatureAlgorithm.RS256)
                .setIssuer("www.php25.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7200 * 1000))

                .setHeader(Maps.toMap(Lists.newArrayList("authorities"), s -> Lists.newArrayList(Role.MEDIA_SERVICE_IMAGE.name())))
                .setSubject("test")
                .compact();
    }

}
