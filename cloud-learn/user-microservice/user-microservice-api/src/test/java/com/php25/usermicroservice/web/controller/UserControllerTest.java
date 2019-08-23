package com.php25.usermicroservice.web.controller;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.vo.req.ReqChangePasswordVo;
import com.php25.usermicroservice.web.vo.req.ReqRegisterUserVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

/**
 * @author: penghuiping
 * @date: 2018/10/12 09:54
 * @description:
 */
@Slf4j
@Component
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    public void register(AllTest allTest) throws Exception {
        ReqRegisterUserVo reqRegisterUserVo = new ReqRegisterUserVo();
        reqRegisterUserVo.setUsername(ConstantTest.Customer.username);
        reqRegisterUserVo.setNickname(ConstantTest.Customer.nickname);
        reqRegisterUserVo.setMobile(ConstantTest.Customer.mobile);
        reqRegisterUserVo.setEmail(ConstantTest.Customer.email);
        reqRegisterUserVo.setPassword(ConstantTest.Customer.password);
        reqRegisterUserVo.setAppId(ConstantTest.Customer.appId);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                        .content(JsonUtil.toJson(reqRegisterUserVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("user_register",
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


    public void changePassword(AllTest allTest) throws Exception {
        ReqChangePasswordVo reqChangePasswordVo = new ReqChangePasswordVo();
        reqChangePasswordVo.setNewPassword(ConstantTest.Customer.newPassword);
        reqChangePasswordVo.setOriginPassword(ConstantTest.Customer.password);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/changePassword")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqChangePasswordVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_changePassword",
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

        Optional<User> userOptional = userRepository.findByUsername(ConstantTest.Customer.username);
        User user = userOptional.get();
        Assertions.assertThat(user.getPassword()).isEqualTo(ConstantTest.Customer.newPassword);
    }


    public void detailInfo(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/user/detailInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_detailInfo",
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
                                .andWithPrefix("roles[].",
                                        fieldWithPath("roleId").description("角色id").optional().type(Long.class),
                                        fieldWithPath("name").description("角色名").optional().type(String.class)
                                ).andWithPrefix("groups[].",
                                        fieldWithPath("groupId").description("组id").optional().type(Long.class),
                                        fieldWithPath("name").description("组名").optional().type(String.class)
                                ).andWithPrefix("apps[].",
                                        fieldWithPath("appId").description("应用id").optional().type(String.class),
                                        fieldWithPath("appName").description("应用名").optional().type(String.class)
                                )
                )).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
        log.info("/user/detailInfo:{}", result);
    }


    public void query(AllTest allTest) throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/queryPage")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_queryPage",
                        requestFields(
                                fieldWithPath("pageNum").description("当前第几页"),
                                fieldWithPath("pageSize").description("每页的数量"),
                                fieldWithPath("searchParamVoList").description("搜索参数")
                        ).andWithPrefix("searchParamVoList[].",
                                fieldWithPath("fieldName").description("搜索字段名,支持username,mobile,id").optional().type(String.class),
                                fieldWithPath("value").description("字段值").optional().type(String.class),
                                fieldWithPath("operator").description("操作支持:EQ,NE,LIKE,GT,LT,GTE,LTE,IN,NIN").optional().type(String.class)
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
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
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
