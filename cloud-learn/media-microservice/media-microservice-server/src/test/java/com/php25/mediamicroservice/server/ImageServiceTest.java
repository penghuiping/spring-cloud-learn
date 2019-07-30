package com.php25.mediamicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.bo.res.StringRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImageServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void findOne() {
        IdStringReq idStringReq = new IdStringReq();
        idStringReq.setId("1");

        var result = webTestClient.post().uri("/img/findOne")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idStringReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ImgBoRes.class);

        log.info("/img/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody().getReturnObject()));
    }

    @Test
    public void findAll() {
        IdsStringReq idsStringReq = new IdsStringReq();
        idsStringReq.setIds(List.of("1","2"));

        var result = webTestClient.post().uri("/img/findAll")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsStringReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ImgBoListRes.class);

        log.info("/img/findAll:{}", JsonUtil.toJson(result.returnResult().getResponseBody().getReturnObject()));
    }

    @Test
    public void save() {
        Base64ImageBo base64ImageBo= new Base64ImageBo();
        var result = webTestClient.post().uri("/img/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(base64ImageBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(StringRes.class);

        log.info("/img/save:{}", JsonUtil.toJson(result.returnResult().getResponseBody().getReturnObject()));
    }
}
