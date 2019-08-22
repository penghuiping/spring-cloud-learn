package com.php25.usermicroservice.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.vo.req.ReqChangePasswordVo;
import com.php25.usermicroservice.web.vo.req.ReqRegisterUserVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

/**
 * @author: penghuiping
 * @date: 2018/10/12 09:54
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "development")
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.DEFAULT)
public class UserControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy filterChainProxy;

    private MockMvc mockMvc;

    private static final String username = "jack";
    private static final String nickname = "jack";
    private static final String password = "123456";
    private static final String newPassword = "654321";
    private static final String mobile = "18812345678";
    private static final String email = "123@qq.com";

    private static final String appId = "#ajduund";



    @Autowired
    private UserRepository userRepository;

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

    public void clean() {
        Optional<User> userOptional = userRepository.findByUsername("jack");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
        }
    }

    @Test
    public void allTest() throws Exception {
        clean();
        register();
        changePassword();
        detailInfo();
        clean();
    }

    public void register() throws Exception {
        ReqRegisterUserVo reqRegisterUserVo = new ReqRegisterUserVo();
        reqRegisterUserVo.setUsername(username);
        reqRegisterUserVo.setNickname(nickname);
        reqRegisterUserVo.setMobile(mobile);
        reqRegisterUserVo.setEmail(email);
        reqRegisterUserVo.setPassword(password);
        reqRegisterUserVo.setAppId(appId);
        String result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                        .content(JsonUtil.toJson(reqRegisterUserVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("register",
                        requestFields(
                                fieldWithPath("username").description("用户名"),
                                fieldWithPath("nickname").description("昵称"),
                                fieldWithPath("mobile").description("手机"),
                                fieldWithPath("password").description("密码"),
                                fieldWithPath("email").description("邮箱"),
                                fieldWithPath("appId").description("应用id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("").ignored(),
                                fieldWithPath("returnObject").description("true:注册成功,false:注册失败"),
                                fieldWithPath("message").description("").ignored())))
                .andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/register:{}", result);
    }


    public void changePassword() throws Exception {
        ReqChangePasswordVo reqChangePasswordVo = new ReqChangePasswordVo();
        reqChangePasswordVo.setNewPassword(newPassword);
        reqChangePasswordVo.setOriginPassword(password);

        String result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/changePassword")
                        .content(JsonUtil.toJson(reqChangePasswordVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("changePassword",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("originPassword").description("原始密码"),
                                fieldWithPath("newPassword").description("新密码")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回").ignored(),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("").ignored()
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/changePassword:{}", result);

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        Assertions.assertThat(user.getPassword()).isEqualTo(newPassword);
    }


    public void detailInfo() throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        String result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/detailInfo")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("detailInfo",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token"))
                        , responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("id").description("用户id"),
                                fieldWithPath("username").description("用户名"),
                                fieldWithPath("nickname").description("昵称"),
                                fieldWithPath("email").description("邮箱"),
                                fieldWithPath("mobile").description("手机"),
                                fieldWithPath("createDate").description("创建日期"),
                                fieldWithPath("lastModifiedDate").description("最后修改日期"),
                                fieldWithPath("roles").description("角色列表"),
                                fieldWithPath("groups").description("组列表"),
                                fieldWithPath("apps").description("所属应用列表"))
//                                .andWithPrefix("roles[].",
//                                        fieldWithPath("roleId").description("角色id").optional(),
//                                        fieldWithPath("name").description("角色名").optional()
//                                ).andWithPrefix("groups[].",
//                                        fieldWithPath("groupId").description("组id").optional(),
//                                        fieldWithPath("name").description("组名").optional()
//                                ).andWithPrefix("apps[].",
//                                        fieldWithPath("appId").description("应用id").optional(),
//                                        fieldWithPath("appName").description("应用名").optional()
//                                )
                )).andReturn().getResponse().getContentAsString();
        log.info("/user/detailInfo:{}", result);
    }


    public void query() throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);

        String result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/queryPage")
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("queryPage",
                        requestFields(
                                fieldWithPath("pageNum").description("当前第几页"),
                                fieldWithPath("pageSize").description("每页的数量"),
                                fieldWithPath("searchParamVoList").description("搜索参数")
                        ).andWithPrefix("searchParamVoList[].",
                                fieldWithPath("fieldName").description("搜索字段名,支持username,mobile,id"),
                                fieldWithPath("value").description("字段值"),
                                fieldWithPath("operator").description("操作支持:EQ,NE,LIKE,GT,LT,GTE,LTE,IN,NIN")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("id").description("用户id"),
                                fieldWithPath("username").description("用户名"),
                                fieldWithPath("nickname").description("昵称"),
                                fieldWithPath("email").description("邮箱"),
                                fieldWithPath("mobile").description("手机"),
                                fieldWithPath("createDate").description("创建日期"),
                                fieldWithPath("lastModifiedDate").description("最后修改日期"),
                                fieldWithPath("enable").description("0:无效,1:有效,2:软删除")
                        )))
                .andReturn().getResponse().getContentAsString();
        log.info("/user/queryPage:{}", result);
    }

//
//
//    //    @Test
//    public void softDelete() {
//        ReqIdsLong idsLongReq = new ReqIdsLong();
//        idsLongReq.setIds(List.of(207156698513670144L));
//
//        var result = webTestClient.post().uri("/adminUser/softDelete")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .syncBody(idsLongReq)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(ResBoolean.class);
//
//        log.info("/adminUser/softDelete:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
//    }
//
//

}
