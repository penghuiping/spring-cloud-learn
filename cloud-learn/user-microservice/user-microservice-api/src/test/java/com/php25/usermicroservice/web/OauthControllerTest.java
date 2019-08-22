package com.php25.usermicroservice.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:12
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "development")
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.DEFAULT)
public class OauthControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy filterChainProxy;

    private MockMvc mockMvc;

    private static final String username = "jack";
    private static final String password = "123456";
    private static final String appId = "#ajduund";
    private static final String appSecret = "123456";
    private static final String appRedirectUrl = "http://www.test.com/callback";

    private String code;

    private String accessToken;

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

    @Test
    public void allTest() throws Exception {
        oauth2Code();
        oauth2Token();
    }

    public void oauth2Code() throws Exception {
        String redirectedUrl = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .param("client_id", appId)
                        .param("response_type", "code")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(appRedirectUrl + "?code=*"))
                .andDo(document("oauth2Code",
                        requestHeaders(headerWithName("Authorization").description("Http基本认证,对应用户在认证服务器注册的用户名与密码")),
                        requestParameters(
                                parameterWithName("client_id").description("在认证服务器注册时分配的appId"),
                                parameterWithName("response_type").description("固定填写code")
                        ), responseHeaders(
                                headerWithName("Location").description("会重定向到app在认证服务注册的重定向回调地址并在url后面拼上code参数如:http://www.test.com?code=12345")
                        ))).andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        this.code = code1;
    }

    public void oauth2Token() throws Exception {
        String result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .param("grant_type", "authorization_code")
                        .param("code", code)
                        .param("client_id", appId)
                        .param("client_secret", appSecret)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("oauth2Token",
                        requestHeaders(headerWithName("Authorization").description("Http基本认证,对应应用接入认证服务器时生成的appId与appSecret")),
                        requestParameters(
                                parameterWithName("grant_type").description("固定填写authorization_code"),
                                parameterWithName("code").description("在/oauth/authorize接口中获取的code"),
                                parameterWithName("client_id").description("在认证服务器注册时候分配的appId"),
                                parameterWithName("client_secret").description("在认证服务器注册的时候分配的appSecret")
                        ), responseFields(
                                fieldWithPath("access_token").description("oauth2的token,此令牌在请求接口数据需要用到"),
                                fieldWithPath("token_type").description("令牌类型"),
                                fieldWithPath("expires_in").description("令牌过期时间"),
                                fieldWithPath("scope").description("固定填写authentication")
                        ))).andReturn().getResponse().getContentAsString();

        Map<String, String> map = JsonUtil.fromJson(result, new TypeReference<Map<String, String>>() {
        });
        this.accessToken = map.get("access_token");
        log.info("access_token:{}", accessToken);
    }
}
