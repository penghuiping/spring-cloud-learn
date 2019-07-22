package com.php25.usermicroservice.server.service;

import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.SearchBoParam;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SecureRandom;
import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/11 21:43
 * @description:
 */
public class MsgTest {


    @Test
    public void testQuery() {
        var searchBoParam = new SearchBoParam();
        searchBoParam.setFieldName("name");
        searchBoParam.setOperator(Operator.EQ);
        searchBoParam.setValue("admin");
        var params = List.of(searchBoParam);
        var searchBo = new SearchBo(params, 1, 5, Sort.Direction.ASC, "id");

        var tmp = WebClient.builder().baseUrl("http://localhost:9090")
                .build()
                .post()

                .uri("/adminRole/query")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(searchBo)
                .retrieve()
                .bodyToFlux(AdminRoleBo.class);
        var list = tmp.collectList().block();
        System.out.println(JsonUtil.toPrettyJson(list));
    }

    @Test
    public void testSave() {
        AdminRoleBo adminRoleBo = new AdminRoleBo();
        adminRoleBo.setId(2L);
        adminRoleBo.setName("admin");

        var tmp = WebClient.builder().baseUrl("http://localhost:9090")
                .build()
                .post()
                .uri("/adminRole/save").syncBody(adminRoleBo)
                .retrieve()
                .bodyToMono(AdminRoleBo.class);

        var user = tmp.block();

        System.out.println(JsonUtil.toPrettyJson(user));
    }

    @Test
    public void testFindOne() {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mobile", "18621287361");

        var tmp = WebClient.builder().baseUrl("http://localhost:9091")
                .build()
                .post()
                .uri("/mobileMsg/sendSMS")
                .retrieve()
                .bodyToMono(Boolean.class);

        var user = tmp.block();

        System.out.println(JsonUtil.toPrettyJson(user));


    }


}
