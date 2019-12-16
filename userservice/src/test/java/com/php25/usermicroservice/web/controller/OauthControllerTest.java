package com.php25.usermicroservice.web.controller;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.vo.req.ReqAuthorizeVo;
import com.php25.usermicroservice.web.vo.req.ReqTokenVo;
import com.php25.usermicroservice.web.vo.res.ResTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:12
 * @description:
 */
@Slf4j
@Component
public class OauthControllerTest {

    public void oauth2CodeSuperAdmin(AllTest allTest) throws Exception {
        ReqAuthorizeVo reqAuthorizeVo = new ReqAuthorizeVo();
        reqAuthorizeVo.setAppId(Constants.SuperAdmin.appId);
        reqAuthorizeVo.setUsername(Constants.SuperAdmin.username);
        reqAuthorizeVo.setPassword(Constants.SuperAdmin.password);

        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .content(JsonUtil.toJson(reqAuthorizeVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(Constants.SuperAdmin.appRedirectUrl + "?code=*"))
                .andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
        Assertions.assertThat(allTest.code).isNotBlank();
    }

    public void oauth2TokenSuperAdmin(AllTest allTest) throws Exception {
        ReqTokenVo reqTokenVo = new ReqTokenVo();
        reqTokenVo.setAppId(Constants.SuperAdmin.appId);
        reqTokenVo.setAppSecret(Constants.SuperAdmin.appSecret);
        reqTokenVo.setCode(allTest.code);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .content(JsonUtil.toJson(reqTokenVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);

        ResTokenVo resTokenVo = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), ResTokenVo.class);
        Assertions.assertThat(resTokenVo.getAccessToken()).isNotBlank();
        allTest.accessToken = resTokenVo.getAccessToken();
        log.info("access_token:{}", allTest.accessToken);
        Assertions.assertThat(allTest.accessToken).isNotBlank();
    }

    public void oauth2CodeAdmin(AllTest allTest) throws Exception {
        ReqAuthorizeVo reqAuthorizeVo = new ReqAuthorizeVo();
        reqAuthorizeVo.setAppId(ConstantTest.Customer.appId);
        reqAuthorizeVo.setUsername(allTest.admin_username);
        reqAuthorizeVo.setPassword(allTest.admin_password);

        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .content(JsonUtil.toJson(reqAuthorizeVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(ConstantTest.Customer.appRedirectUrl + "?code=*"))
                .andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
        Assertions.assertThat(allTest.code).isNotBlank();
    }

    public void oauth2TokenAdmin(AllTest allTest) throws Exception {
        ReqTokenVo reqTokenVo = new ReqTokenVo();
        reqTokenVo.setAppId(ConstantTest.Customer.appId);
        reqTokenVo.setAppSecret(ConstantTest.Customer.appSecret);
        reqTokenVo.setCode(allTest.code);
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .content(JsonUtil.toJson(reqTokenVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        ResTokenVo resTokenVo = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), ResTokenVo.class);
        Assertions.assertThat(resTokenVo.getAccessToken()).isNotBlank();
        allTest.accessToken = resTokenVo.getAccessToken();
        log.info("access_token:{}", allTest.accessToken);
        Assertions.assertThat(allTest.accessToken).isNotBlank();
    }


    public void oauth2Code(AllTest allTest) throws Exception {
        ReqAuthorizeVo reqAuthorizeVo = new ReqAuthorizeVo();
        reqAuthorizeVo.setAppId(ConstantTest.Customer.appId);
        reqAuthorizeVo.setUsername(ConstantTest.Customer.username);
        reqAuthorizeVo.setPassword(ConstantTest.Customer.password);

        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .content(JsonUtil.toJson(reqAuthorizeVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(ConstantTest.Customer.appRedirectUrl + "?code=*"))
                .andDo(document("oauth2Code",
                        requestFields(
                                fieldWithPath("appId").description("在认证服务器注册时分配的appId"),
                                fieldWithPath("username").description("用户在认证服务器注册的用户名"),
                                fieldWithPath("password").description("用户在认证服务器注册的密码")
                        ), responseHeaders(
                                headerWithName("Location").description("会重定向到app在认证服务注册的重定向回调地址并在url后面拼上code参数如:http://www.test.com?code=12345")
                        ))).andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
        Assertions.assertThat(allTest.code).isNotBlank();
    }

    public void oauth2Token(AllTest allTest) throws Exception {
        ReqTokenVo reqTokenVo = new ReqTokenVo();
        reqTokenVo.setAppId(ConstantTest.Customer.appId);
        reqTokenVo.setAppSecret(ConstantTest.Customer.appSecret);
        reqTokenVo.setCode(allTest.code);

        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .content(JsonUtil.toJson(reqTokenVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("oauth2Token",
                        requestFields(
                                fieldWithPath("appId").description("在认证服务器注册时分配的appId"),
                                fieldWithPath("appSecret").description("在认证服务器注册的时候分配的appSecret"),
                                fieldWithPath("code").description("在/oauth/authorize接口中获取的code")
                        ), responseFields(
                                fieldWithPath("errorCode").description("错误码,0:正常，0以外都是非正常"),
                                fieldWithPath("returnObject").description("返回对象内容"),
                                fieldWithPath("returnObject.accessToken").description("oauth2的accessToken,此令牌在请求接口数据需要用到"),
                                fieldWithPath("returnObject.refreshToken").description("oauth2的refreshToken,此令牌在请求接口数据需要用到"),
                                fieldWithPath("returnObject.expiresIn").description("令牌过期时间"),
                                fieldWithPath("returnObject.jti").description("jwt的唯一标识"),
                                fieldWithPath("message").description("错误码描述")

                        ))).andReturn().getResponse().getContentAsString();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        ResTokenVo resTokenVo = JsonUtil.fromJson(JsonUtil.toJson(jsonResponse.getReturnObject()), ResTokenVo.class);
        Assertions.assertThat(resTokenVo.getAccessToken()).isNotBlank();
        allTest.accessToken = resTokenVo.getAccessToken();
        log.info("access_token:{}", allTest.accessToken);
        Assertions.assertThat(allTest.accessToken).isNotBlank();
    }
}
