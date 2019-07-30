package com.php25.notifymicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Value("${spring.mail.username}")
    private String mailAddress;

    @Test
    public void sendSimpleMail() {
        SendSimpleMailReq sendSimpleMailReq = new SendSimpleMailReq();
        sendSimpleMailReq.setContent("你好");
        sendSimpleMailReq.setTitle("测试邮件");
        sendSimpleMailReq.setSendTo(mailAddress);

        var result = webTestClient.post().uri("/mail/sendSimpleMail")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(sendSimpleMailReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/mail/sendSimpleMail:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    @Test
    public void sendAttachmentsMail() {
        SendAttachmentsMailReq sendAttachmentsMailReq = new SendAttachmentsMailReq();
        sendAttachmentsMailReq.setContent("你好");
        sendAttachmentsMailReq.setTitle("测试邮件");
        sendAttachmentsMailReq.setSendTo(mailAddress);
        Pair pair = new Pair<>();
        pair.setKey("附件1");
        pair.setValue("");
        sendAttachmentsMailReq.setAttachments(List.of(pair));

        var result = webTestClient.post().uri("/mail/sendAttachmentsMail")
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
