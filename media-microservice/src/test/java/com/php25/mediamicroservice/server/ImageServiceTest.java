package com.php25.mediamicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ReqIdString;
import com.php25.common.flux.web.ReqIdsString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    //    @Test
    public void findOne() {
        ReqIdString idStringReq = new ReqIdString();
        idStringReq.setId("1");

        WebTestClient.BodySpec result = webTestClient.post().uri("/img/findOne")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idStringReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class);

        log.info("/img/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    //    @Test
    public void findAll() {
        ReqIdsString idsStringReq = new ReqIdsString();
        idsStringReq.setIds(Lists.newArrayList("1", "2"));

        WebTestClient.BodySpec result = webTestClient.post().uri("/img/findAll")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsStringReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class);

        log.info("/img/findAll:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    //    @Test
    public void save() {
        WebTestClient.BodySpec result = webTestClient.post().uri("/img/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(null)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class);

        log.info("/img/save:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }
}
