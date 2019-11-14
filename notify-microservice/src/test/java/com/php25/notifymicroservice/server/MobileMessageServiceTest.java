package com.php25.notifymicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.notifymicroservice.NotifyServiceApplicationTest;
import com.php25.notifymicroservice.server.vo.req.SendSMSReq;
import com.php25.notifymicroservice.server.vo.req.ValidateSMSReq;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@Component
public class MobileMessageServiceTest {

    public void sendSMS(NotifyServiceApplicationTest notifyServiceApplicationTest) {
        SendSMSReq sendSMSReq = new SendSMSReq();
        sendSMSReq.setMobile("18812345678");

        String result = notifyServiceApplicationTest.webTestClient.post().uri("/mobile/sendSMS")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + notifyServiceApplicationTest.jwt)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(sendSMSReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class).consumeWith(document("mobileMsg_sendSMS",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("mobile").description("手机")
                        ), responseFields(
                                fieldWithPath("errorCode").description("错误码:0为正常;0以外都是非正常"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误描述").type("String"))
                )).returnResult().getResponseBody();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);

        log.info("/mobileMsg/sendSMS:{}", result);
    }

    public void validateSMS(NotifyServiceApplicationTest notifyServiceApplicationTest) {
        ValidateSMSReq validateSMSReq = new ValidateSMSReq();
        validateSMSReq.setMobile("18812345678");
        validateSMSReq.setMsgCode("1111");

        String result = notifyServiceApplicationTest.webTestClient.post().uri("/mobile/validateSMS")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + notifyServiceApplicationTest.jwt)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(validateSMSReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class).consumeWith(document("mobileMsg_validateSMS",
                        requestHeaders(headerWithName("Authorization").description("放入/oauth2/token接口拿到的access_token")),
                        requestFields(
                                fieldWithPath("mobile").description("手机"),
                                fieldWithPath("msgCode").description("短信")
                        ), responseFields(
                                fieldWithPath("errorCode").description("错误码:0为正常;0以外都是非正常"),
                                fieldWithPath("returnObject").description("true:成功,false:失败"),
                                fieldWithPath("message").description("错误描述").type("String"))
                )).returnResult().getResponseBody();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);

        log.info("/mobileMsg/validateSMS:{}", result);

    }


}
