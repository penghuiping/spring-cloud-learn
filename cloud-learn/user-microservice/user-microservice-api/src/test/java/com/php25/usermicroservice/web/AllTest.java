package com.php25.usermicroservice.web;

import com.php25.usermicroservice.web.controller.AppClientControllerTest;
import com.php25.usermicroservice.web.controller.OauthControllerTest;
import com.php25.usermicroservice.web.controller.RoleControllerTest;
import com.php25.usermicroservice.web.controller.UserControllerTest;
import com.php25.usermicroservice.web.repository.AppRepository;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author: penghuiping
 * @date: 2019/8/22 16:11
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "development")
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.DEFAULT)
public class AllTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

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

    public String code;

    public String accessToken;

    public Long roleId;

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


    @After
    public void after() {
        clean();
    }


    public void clean() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        appRepository.deleteAll();
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


        //注销app应用,使用super_admin账号
        oauthControllerTest.oauth2CodeSuperAdmin(this);
        oauthControllerTest.oauth2TokenSuperAdmin(this);
        appClientControllerTest.unregister(this);
    }
}
