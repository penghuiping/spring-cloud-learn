package com.php25.usermicroservice.web;

import com.php25.usermicroservice.web.controller.AppClientControllerTest;
import com.php25.usermicroservice.web.controller.GroupControllerTest;
import com.php25.usermicroservice.web.controller.OauthControllerTest;
import com.php25.usermicroservice.web.controller.RoleControllerTest;
import com.php25.usermicroservice.web.controller.UserControllerTest;
import com.php25.usermicroservice.web.repository.AppRepository;
import com.php25.usermicroservice.web.repository.GroupRepository;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author: penghuiping
 * @date: 2019/8/22 16:11
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test-postgres")
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.DEFAULT)
public class AllTest {

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

    static {
        redis.setPortBindings(Lists.newArrayList("6379:6379"));
        rabbitmq.setPortBindings(Lists.newArrayList("5672:5672"));
        postgres.setPortBindings(Lists.newArrayList("5432:5432"));
    }


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy filterChainProxy;

    public MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public String code;

    public String accessToken;

    public Long roleId;
    public Long userId;
    public Long groupId;

    public String admin_username;
    public String admin_password;

    @Autowired
    private OauthControllerTest oauthControllerTest;

    @Autowired
    private UserControllerTest userControllerTest;

    @Autowired
    private AppClientControllerTest appClientControllerTest;

    @Autowired
    private RoleControllerTest roleControllerTest;

    @Autowired
    private GroupControllerTest groupControllerTest;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .addFilter(filterChainProxy)
                .build();

    }

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @After
    public void after() {
        clean();
    }

    public void clean() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        appRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void test() throws Exception {
        //使用super_admin账号
        oauthControllerTest.oauth2CodeSuperAdmin(this);
        oauthControllerTest.oauth2TokenSuperAdmin(this);
        appClientControllerTest.register(this);
        appClientControllerTest.detailInfo(this);
        appClientControllerTest.queryPage(this);

        //使用customer账号
        userControllerTest.register(this);
        oauthControllerTest.oauth2Code(this);
        oauthControllerTest.oauth2Token(this);
        userControllerTest.detailInfo(this);
        userControllerTest.changePassword(this);

        //使用admin账号
        oauthControllerTest.oauth2CodeAdmin(this);
        oauthControllerTest.oauth2TokenAdmin(this);
        userControllerTest.query(this);
        roleControllerTest.create(this);
        roleControllerTest.queryPage(this);
        roleControllerTest.changeInfo(this);
        roleControllerTest.detailInfo(this);
        roleControllerTest.unableRole(this);
        groupControllerTest.create(this);
        groupControllerTest.queryPage(this);
        groupControllerTest.changeInfo(this);
        groupControllerTest.detailInfo(this);
        groupControllerTest.unableGroup(this);

        userControllerTest.authorizeRole(this);
        userControllerTest.revokeRole(this);
        userControllerTest.joinGroup(this);
        userControllerTest.leaveGroup(this);

        //注销app应用,使用super_admin账号
        oauthControllerTest.oauth2CodeSuperAdmin(this);
        oauthControllerTest.oauth2TokenSuperAdmin(this);
        appClientControllerTest.unregister(this);
    }
}
