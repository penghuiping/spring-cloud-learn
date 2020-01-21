package com.php25.usermicroservice.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.db.specification.Operator;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.common.flux.web.ReqIdLong;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.vo.req.ReqCreateGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqGroupChangeInfoVo;
import com.php25.usermicroservice.web.vo.req.SearchParamVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResGroupPageVo;
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

/**
 * @author: penghuiping
 * @date: 2019/8/23 10:37
 * @description:
 */
@Slf4j
@Component
public class GroupControllerTest {

    public void create(AllTest allTest) throws Exception {
        ReqCreateGroupVo reqCreateGroupVo = new ReqCreateGroupVo();
        reqCreateGroupVo.setName(ConstantTest.Customer.groupName);
        reqCreateGroupVo.setDescription(ConstantTest.Customer.groupName);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/group/create")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqCreateGroupVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("group_create",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
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
        log.info("/group/create:{}", result);
    }

    public void queryPage(AllTest allTest) throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);
        searchVo.setSearchParamVoList(Lists.newArrayList(new SearchParamVo("name","selfDefineGroup", Operator.EQ)));

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/group/queryPage")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("group_queryPage",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
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
                                fieldWithPath("id").description("组id"),
                                fieldWithPath("name").description("组名"),
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
        Assertions.assertThat(jsonResponse.getReturnObject()).asList().hasSize(1);
        log.info("/group/queryPage:{}", result);

        List<ResGroupPageVo> resGroupPageVos = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), new TypeReference<List<ResGroupPageVo>>() {
        });
        allTest.groupId = resGroupPageVos.get(0).getId();
    }


    public void changeInfo(AllTest allTest) throws Exception {
        ReqGroupChangeInfoVo reqGroupChangeInfoVo = new ReqGroupChangeInfoVo();
        reqGroupChangeInfoVo.setGroupId(allTest.groupId);
        reqGroupChangeInfoVo.setDescription("changedRoleDescription");
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/group/changeInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqGroupChangeInfoVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("group_changeInfo",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestFields(
                                fieldWithPath("groupId").description("组id"),
                                fieldWithPath("description").description("描述")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/group/changeInfo:{}", result);
    }


    public void detailInfo(AllTest allTest) throws Exception {
        ReqIdLong reqIdLong = new ReqIdLong();
        reqIdLong.setId(allTest.groupId);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/group/detailInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqIdLong))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("group_detailInfo",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestFields(
                                fieldWithPath("id").description("组id")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("id").description("组id"),
                                fieldWithPath("name").description("组名"),
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
        log.info("/group/detailInfo:{}", result);
    }

    public void unableGroup(AllTest allTest) throws Exception {
        ReqIdLong reqIdLong = new ReqIdLong();
        reqIdLong.setId(allTest.groupId);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/group/unableGroup")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqIdLong))
                        .param("groupId", allTest.groupId + "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("group_unableGroup",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestFields(
                                fieldWithPath("id").description("组id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:成功返回"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误码描述").type("String")
                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/group/unableGroup:{}", result);
    }
}
