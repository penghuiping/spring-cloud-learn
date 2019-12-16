package com.php25.usermicroservice.web.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.vo.req.ReqAuthorizeRoleVo;
import com.php25.usermicroservice.web.vo.req.ReqChangePasswordVo;
import com.php25.usermicroservice.web.vo.req.ReqJoinGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqLeaveGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqRegisterUserVo;
import com.php25.usermicroservice.web.vo.req.ReqRevokeRoleVo;
import com.php25.usermicroservice.web.vo.req.SearchParamVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResUserDetailVo;
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:注册成功,false:注册失败"),
                                fieldWithPath("message").description("错误码描述").type("String"))))
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_changePassword",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("originPassword").description("原始密码"),
                                fieldWithPath("newPassword").description("新密码")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
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
                                fieldWithPath("roles").description("角色列表").type("List"),
                                fieldWithPath("groups").description("组列表").type("List"),
                                fieldWithPath("apps").description("所属应用列表").type("List"))
                                .andWithPrefix("roles[].",
                                        fieldWithPath("roleId").description("角色id").optional().type("Number"),
                                        fieldWithPath("name").description("角色名").optional().type("String")
                                ).andWithPrefix("groups[].",
                                        fieldWithPath("groupId").description("组id").optional().type("Number"),
                                        fieldWithPath("name").description("组名").optional().type("String")
                                ).andWithPrefix("apps[].",
                                        fieldWithPath("appId").description("应用id").optional().type("Number"),
                                        fieldWithPath("appName").description("应用名").optional().type("String")
                                )
                )).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
        log.info("/user/detailInfo:{}", result);

        ResUserDetailVo resUserDetailVo = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), ResUserDetailVo.class);
        allTest.userId = resUserDetailVo.getId();
    }


    public void query(AllTest allTest) throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);

        SearchParamVo searchParamVo = new SearchParamVo("username","jack", Operator.EQ);
        searchVo.setSearchParamVoList(Lists.newArrayList(searchParamVo));

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/queryPage")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_queryPage",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("pageNum").description("当前第几页"),
                                fieldWithPath("pageSize").description("每页的数量"),
                                fieldWithPath("searchParamVoList").description("搜索参数").type("List")
                        ).andWithPrefix("searchParamVoList[].",
                                fieldWithPath("fieldName").description("搜索字段名,支持username,mobile,id").optional().type("String"),
                                fieldWithPath("value").description("字段值").optional().type("String"),
                                fieldWithPath("operator").description("操作支持:EQ,NE,LIKE,GT,LT,GTE,LTE,IN,NIN").optional().type("String")
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


    public void revokeRole(AllTest allTest) throws Exception {
        ReqRevokeRoleVo reqRevokeRoleVo = new ReqRevokeRoleVo();
        reqRevokeRoleVo.setRoleId(allTest.roleId);
        reqRevokeRoleVo.setUserId(allTest.userId);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/revokeRole")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqRevokeRoleVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_revokeRole",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("roleId").description("角色id"),
                                fieldWithPath("userId").description("用户id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/admin/revokeRole:{}", result);
    }

    public void authorizeRole(AllTest allTest) throws Exception {
        ReqAuthorizeRoleVo reqAuthorizeRoleVo = new ReqAuthorizeRoleVo();
        reqAuthorizeRoleVo.setRoleId(allTest.roleId);
        reqAuthorizeRoleVo.setUserId(allTest.userId);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/authorizeRole")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqAuthorizeRoleVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_authorizeRole",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("roleId").description("角色id"),
                                fieldWithPath("userId").description("用户id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/admin/authorizeRole:{}", result);
    }

    public void joinGroup(AllTest allTest) throws Exception {
        ReqJoinGroupVo reqJoinGroupVo = new ReqJoinGroupVo();
        reqJoinGroupVo.setGroupId(allTest.groupId);
        reqJoinGroupVo.setUserId(allTest.userId);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/joinGroup")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqJoinGroupVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_joinGroup",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("groupId").description("组id"),
                                fieldWithPath("userId").description("用户id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/admin/joinGroup:{}", result);
    }


    public void leaveGroup(AllTest allTest) throws Exception {
        ReqLeaveGroupVo reqLeaveGroupVo = new ReqLeaveGroupVo();
        reqLeaveGroupVo.setGroupId(allTest.groupId);
        reqLeaveGroupVo.setUserId(allTest.userId);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/admin/leaveGroup")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqLeaveGroupVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user_admin_leaveGroup",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("groupId").description("组id"),
                                fieldWithPath("userId").description("用户id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/user/admin/leaveGroup:{}", result);
    }


}
