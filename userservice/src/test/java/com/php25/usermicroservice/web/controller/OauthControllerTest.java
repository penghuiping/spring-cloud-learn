package com.php25.usermicroservice.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.web.AllTest;
import com.php25.usermicroservice.web.ConstantTest;
import com.php25.usermicroservice.web.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
@Component
public class OauthControllerTest {

    public void oauth2CodeSuperAdmin(AllTest allTest) throws Exception {
        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .param("client_id", Constants.SuperAdmin.appId)
                        .param("response_type", "code")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(Constants.SuperAdmin.username, Constants.SuperAdmin.password))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(Constants.SuperAdmin.appRedirectUrl + "?code=*"))
                .andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
    }

    public void oauth2TokenSuperAdmin(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .param("grant_type", "authorization_code")
                        .param("code", allTest.code)
                        .param("client_id", Constants.SuperAdmin.appId)
                        .param("client_secret", Constants.SuperAdmin.appSecret)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> map = JsonUtil.fromJson(result, new TypeReference<Map<String, String>>() {
        });
        allTest.accessToken = map.get("access_token");
        log.info("access_token:{}", allTest.accessToken);
    }

    public void oauth2CodeAdmin(AllTest allTest) throws Exception {
        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .param("client_id", ConstantTest.Customer.appId)
                        .param("response_type", "code")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(allTest.admin_username, allTest.admin_password))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(ConstantTest.Customer.appRedirectUrl + "?code=*"))
                .andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
    }

    public void oauth2TokenAdmin(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .param("grant_type", "authorization_code")
                        .param("code", allTest.code)
                        .param("client_id", ConstantTest.Customer.appId)
                        .param("client_secret", ConstantTest.Customer.appSecret)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> map = JsonUtil.fromJson(result, new TypeReference<Map<String, String>>() {
        });
        allTest.accessToken = map.get("access_token");
        log.info("access_token:{}", allTest.accessToken);
    }


    public void oauth2Code(AllTest allTest) throws Exception {
        String redirectedUrl = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/authorize")
                        .param("client_id", ConstantTest.Customer.appId)
                        .param("response_type", "code")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ConstantTest.Customer.username, ConstantTest.Customer.password))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(ConstantTest.Customer.appRedirectUrl + "?code=*"))
                .andDo(document("oauth2Code",
                        requestHeaders(headerWithName("Authorization").description("Http基本认证,内容对应用户在认证服务器注册的用户名与密码,例如:base64(${username}:${password})")),
                        requestParameters(
                                parameterWithName("client_id").description("在认证服务器注册时分配的appId"),
                                parameterWithName("response_type").description("固定填写code")
                        ), responseHeaders(
                                headerWithName("Location").description("会重定向到app在认证服务注册的重定向回调地址并在url后面拼上code参数如:http://www.test.com?code=12345")
                        ))).andReturn().getResponse().getRedirectedUrl();
        log.info("redirectUrl:{}", redirectedUrl);
        String code1 = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);
        log.info("code:{}", code1);
        allTest.code = code1;
    }

    public void oauth2Token(AllTest allTest) throws Exception {
        String result = allTest.mockMvc.perform(
                MockMvcRequestBuilders.post("/oauth2/token")
                        .param("grant_type", "authorization_code")
                        .param("code", allTest.code)
                        .param("client_id", ConstantTest.Customer.appId)
                        .param("client_secret", ConstantTest.Customer.appSecret)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("oauth2Token",
                        requestParameters(
                                parameterWithName("grant_type").description("固定填写authorization_code"),
                                parameterWithName("code").description("在/oauth/authorize接口中获取的code"),
                                parameterWithName("client_id").description("在认证服务器注册时候分配的appId"),
                                parameterWithName("client_secret").description("在认证服务器注册的时候分配的appSecret")
                        ), responseFields(
                                fieldWithPath("access_token").description("oauth2的token,此令牌在请求接口数据需要用到"),
                                fieldWithPath("token_type").description("令牌类型"),
                                fieldWithPath("expires_in").description("令牌过期时间"),
                                fieldWithPath("scope").description("固定填写authentication"),
                                fieldWithPath("jti").description("jwt的唯一标识")
                        ))).andReturn().getResponse().getContentAsString();

        Map<String, String> map = JsonUtil.fromJson(result, new TypeReference<Map<String, String>>() {
        });
        allTest.accessToken = map.get("access_token");
        log.info("access_token:{}", allTest.accessToken);
    }
}
