package com.php25.usermicroservice.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.vo.req.ReqRegisterAppVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResAccountVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

/**
 * @author: penghuiping
 * @date: 2019/8/22 16:31
 * @description:
 */
@Slf4j
@Component
public class AppClientControllerTest {

    public void register(AllTest allTest) throws Exception {
        ReqRegisterAppVo reqRegisterAppVo = new ReqRegisterAppVo();
        reqRegisterAppVo.setAppId(ConstantTest.Customer.appId);
        reqRegisterAppVo.setAppSecret(ConstantTest.Customer.appSecret);
        reqRegisterAppVo.setRegisteredRedirectUri(ConstantTest.Customer.appRedirectUrl);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/appClient/register")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(reqRegisterAppVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("appClient_register",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestFields(
                                fieldWithPath("appId").description("应用id"),
                                fieldWithPath("appSecret").description("应用秘钥"),
                                fieldWithPath("registeredRedirectUri").description("应用获取code回调地址")
                        ), responseFields(
                                fieldWithPath("errorCode").description("错误码:0为正常"),
                                fieldWithPath("returnObject").description("app管理员账号"),
                                fieldWithPath("message").description("错误描述").type("String"))
                                .andWithPrefix("returnObject.",
                                        fieldWithPath("username").description("用户名"),
                                        fieldWithPath("password").description("密码")
                                )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();

        ResAccountVo resAccountVo = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), new TypeReference<ResAccountVo>() {
        });

        allTest.admin_username = resAccountVo.getUsername();
        allTest.admin_password = resAccountVo.getPassword();

        log.info("/appClient/register:{}", result);
    }


    public void queryPage(AllTest allTest) throws Exception {
        SearchVo searchVo = new SearchVo();
        searchVo.setPageNum(1);
        searchVo.setPageSize(5);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/appClient/queryPage")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .content(JsonUtil.toJson(searchVo))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("appClient_queryPage",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestFields(
                                fieldWithPath("pageNum").description("当前第几页"),
                                fieldWithPath("pageSize").description("每页的数量"),
                                fieldWithPath("searchParamVoList").description("搜索参数").type("List").optional()
                        ).andWithPrefix("searchParamVoList[].",
                                fieldWithPath("fieldName").description("搜索字段名").optional().type("String"),
                                fieldWithPath("value").description("字段值").optional().type("String"),
                                fieldWithPath("operator").description("操作支持:EQ,NE,LIKE,GT,LT,GTE,LTE,IN,NIN").optional().type("String")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("appId").description("应用id"),
                                fieldWithPath("appName").description("应用名"),
                                fieldWithPath("appSecret").description("应用秘钥"),
                                fieldWithPath("registeredRedirectUri").description("应用获取code回调地址"),
                                fieldWithPath("registerDate").description("注册日期"),
                                fieldWithPath("enable").description("0:无效,1:有效,2:软删除")
                        )))
                .andReturn().getResponse().getContentAsString();
        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).asList().hasSize(2);
        log.info("/appClient/queryPage:{}", result);
    }

    public void detailInfo(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/appClient/detailInfo")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .param("appId", ConstantTest.Customer.appId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("appClient_detailInfo",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestParameters(
                                parameterWithName("appId").description("应用id")
                        ), responseFields(
                                beneathPath("returnObject"),
                                fieldWithPath("appId").description("应用id"),
                                fieldWithPath("appSecret").description("应用秘钥"),
                                fieldWithPath("registeredRedirectUri").description("应用获取code回调地址"))))
                .andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
        log.info("/appClient/detailInfo:{}", result);
    }


    public void unregister(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/appClient/unregister")
                        .header("Authorization", "Bearer " + allTest.accessToken)
                        .param("appId", ConstantTest.Customer.appId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("appClient_unregister",
                        requestHeaders(headerWithName("Authorization").description(ConstantTest.AUTHORIZATION_DESC)),
                        requestParameters(
                                parameterWithName("appId").description("应用id")
                        ), responseFields(
                                fieldWithPath("errorCode").description("0:正确"),
                                fieldWithPath("returnObject").description("true:取消注册成功,false:取消注册失败"),
                                fieldWithPath("message").description("错误码描述").type("String"))))
                .andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);
        log.info("/appClient/unregister:{}", result);
    }
}
