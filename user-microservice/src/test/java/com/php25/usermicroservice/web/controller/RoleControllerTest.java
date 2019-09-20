package com.php25.usermicroservice.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.vo.req.ReqCreateRoleVo;
import com.php25.usermicroservice.web.vo.req.ReqRoleChangeInfoVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResRolePageVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

/**
 * @author: penghuiping
 * @date: 2019/8/23 10:37
 * @description:
 */
@Slf4j
@Component
public class RoleControllerTest {

    public void create(AllTest allTest) throws Exception {
        ReqCreateRoleVo reqCreateRoleVo = new ReqCreateRoleVo();
        reqCreateRoleVo.setName(ConstantTest.Customer.roleName);
        reqCreateRoleVo.setDescription(ConstantTest.Customer.roleName);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/role/create")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqCreateRoleVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("role_create",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("name").description("名字"),
                                fieldWithPath("description").description("描述")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/role/create:{}", result);
    }

    public void queryPage(AllTest allTest) throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/role/queryPage")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("role_queryPage",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("pageNum").description("当前第几页"),
                                fieldWithPath("pageSize").description("每页的数量"),
                                fieldWithPath("searchParamVoList").description("搜索参数").type("List")
                        ).andWithPrefix("searchParamVoList[].",
                                fieldWithPath("fieldName").description("搜索字段名,支持name,id").optional().type("String"),
                                fieldWithPath("value").description("字段值").optional().type("String"),
                                fieldWithPath("operator").description("操作支持:EQ,NE,LIKE,GT,LT,GTE,LTE,IN,NIN").optional().type("String")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("id").description("角色id"),
                                fieldWithPath("name").description("角色名"),
                                fieldWithPath("appId").description("应用id"),
                                fieldWithPath("description").description("描述"),
                                fieldWithPath("createUserId").description("创建者用户id"),
                                fieldWithPath("lastModifiedUserId").description("最后修改者用户id"),
                                fieldWithPath("createDate").description("创建日期"),
                                fieldWithPath("lastModifiedDate").description("最后修改日期"),
                                fieldWithPath("enable").description("0:无效,1:有效,2:软删除")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).asList().hasSize(3);
        log.info("/role/queryPage:{}", result);

        List<ResRolePageVo> resRolePageVos = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), new TypeReference<List<ResRolePageVo>>() {
        });
        allTest.roleId = resRolePageVos.get(2).getId();
    }


    public void changeInfo(AllTest allTest) throws Exception {
        ReqRoleChangeInfoVo reqRoleChangeInfoVo = new ReqRoleChangeInfoVo();
        reqRoleChangeInfoVo.setRoleId(allTest.roleId);
        reqRoleChangeInfoVo.setDescription("changedRoleDescription");
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/role/changeInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqRoleChangeInfoVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("role_changeInfo",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("roleId").description("角色id"),
                                fieldWithPath("description").description("描述")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/role/changeInfo:{}", result);
    }


    public void detailInfo(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/role/detailInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .param("roleId", allTest.roleId + "")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("role_detailInfo",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestParameters(
                                parameterWithName("roleId").description("角色id")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("id").description("角色id"),
                                fieldWithPath("name").description("角色名"),
                                fieldWithPath("appId").description("所属应用id"),
                                fieldWithPath("description").description("描述"),
                                fieldWithPath("createUserId").description("创建者用户id"),
                                fieldWithPath("lastModifiedUserId").description("最后修改者用户id"),
                                fieldWithPath("createDate").description("创建日期"),
                                fieldWithPath("lastModifiedDate").description("最后修改日期"),
                                fieldWithPath("enable").description("0:无效,1:有效,2:软删除")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
        log.info("/role/detailInfo:{}", result);
    }

    public void unableRole(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/role/unableRole")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .param("roleId", allTest.roleId + "")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("role_unableRole",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestParameters(
                                parameterWithName("roleId").description("角色id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/role/unableRole:{}", result);
    }
}
