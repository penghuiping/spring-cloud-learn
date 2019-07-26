package com.php25.usermicroservice.server;

import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.SearchBoParam;
import com.php25.usermicroservice.client.bo.res.AdminUserBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminUserBoRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2018/10/12 09:54
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminUserServiceTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void login() {
        LoginBo loginBo = new LoginBo();
        loginBo.setUsername("jack");
        loginBo.setPassword("123456");

        var result = webTestClient.post().uri("/adminUser/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(loginBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminUserBoRes.class);

        log.info("/adminUser/login:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    @Test
    public void query() {
        var searchBoParam = new SearchBoParam();
        searchBoParam.setFieldName("username");
        searchBoParam.setOperator(Operator.EQ);
        searchBoParam.setValue("jack");
        var params = List.of(searchBoParam);
        var searchBo = new SearchBo(params, 1, 5, Sort.Direction.ASC, "id");

        var result = webTestClient.post().uri("/adminUser/query")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(searchBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminUserBoListRes.class);

        log.info("/adminUser/query:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


}
