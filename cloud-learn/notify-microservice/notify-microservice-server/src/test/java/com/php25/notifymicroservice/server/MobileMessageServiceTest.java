package com.php25.notifymicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MobileMessageServiceTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void sendSMS() {
        SendSMSReq sendSMSReq = new SendSMSReq();
        sendSMSReq.setMobile("18812345678");

        var result = webTestClient.post().uri("/mobileMsg/sendSMS")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(sendSMSReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/mobileMsg/sendSMS:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    @Test
    public void validateSMS() {
        ValidateSMSReq validateSMSReq = new ValidateSMSReq();
        validateSMSReq.setMobile("18812345678");
        validateSMSReq.setMsgCode("1111");

        var result = webTestClient.post().uri("/mobileMsg/validateSMS")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(validateSMSReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/mobileMsg/validateSMS:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));

    }


}
