package com.php25.notifymicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.notifymicroservice.NotifyServiceApplicationTest;
import com.php25.notifymicroservice.server.vo.PairVo;
import com.php25.notifymicroservice.server.vo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.server.vo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.server.vo.res.BooleanRes;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@Component
public class MailServiceTest {


    @Value("${spring.mail.username}")
    private String mailAddress;

    public void sendSimpleMail(NotifyServiceApplicationTest notifyServiceApplicationTest) {
        SendSimpleMailReq sendSimpleMailReq = new SendSimpleMailReq();
        sendSimpleMailReq.setContent("你好");
        sendSimpleMailReq.setTitle("测试邮件");
        sendSimpleMailReq.setSendTo(mailAddress);

        String result = notifyServiceApplicationTest.webTestClient.post().uri("/mail/sendSimpleMail")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(sendSimpleMailReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class).consumeWith(document("sendSimpleMail", requestFields(
                        fieldWithPath("sendTo").description("邮件接收人邮箱"),
                        fieldWithPath("title").description("标题"),
                        fieldWithPath("content").description("内容")
                        ), responseFields(
                        fieldWithPath("errorCode").description("错误码:0为正常;0以外都是非正常"),
                        fieldWithPath("returnObject").description("true:成功,false:失败"),
                        fieldWithPath("message").description("错误描述").type("String"))
                )).returnResult().getResponseBody();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isEqualTo(true);

        log.info("/mail/sendSimpleMail:{}", result);
    }

    public void sendAttachmentsMail(NotifyServiceApplicationTest notifyServiceApplicationTest) {
        SendAttachmentsMailReq sendAttachmentsMailReq = new SendAttachmentsMailReq();
        sendAttachmentsMailReq.setContent("你好");
        sendAttachmentsMailReq.setTitle("测试邮件");
        sendAttachmentsMailReq.setSendTo(mailAddress);
        PairVo<String, String> pair = new PairVo<>();
        pair.setKey("附件1");
        pair.setValue("");
        sendAttachmentsMailReq.setAttachments(Lists.newArrayList(pair));

        WebTestClient.BodySpec result = notifyServiceApplicationTest.webTestClient.post().uri("/mail/sendAttachmentsMail")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(sendAttachmentsMailReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/image/sendAttachmentsMail:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


}
