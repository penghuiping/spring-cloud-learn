package com.php25.usermicroservice.server;

import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.SearchBoParam;
import com.php25.usermicroservice.client.bo.res.AdminMenuButtonBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminRoleServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private IdGeneratorService idGeneratorService;


    @Test
    public void save() {
        AdminRoleBo adminRoleBo = new AdminRoleBo();
        adminRoleBo.setName("admin1");
        adminRoleBo.setDescription("管理员1");
        adminRoleBo.setCreateTime(LocalDateTime.now());
        adminRoleBo.setUpdateTime(LocalDateTime.now());
        adminRoleBo.setEnable(1);

        AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
        adminMenuButtonBo.setId(1L);
        AdminMenuButtonBo adminMenuButtonBo1 = new AdminMenuButtonBo();
        adminMenuButtonBo1.setId(2L);
        AdminMenuButtonBo adminMenuButtonBo3 = new AdminMenuButtonBo();
        adminMenuButtonBo3.setId(3L);
        adminRoleBo.setMenus(List.of(adminMenuButtonBo, adminMenuButtonBo1, adminMenuButtonBo3));


        var result = webTestClient.post().uri("/adminRole/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(adminRoleBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminRoleBoRes.class);

        log.info("/adminRole/save:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));

    }


    @Test
    public void query() {
        var searchBoParam = new SearchBoParam();
        searchBoParam.setFieldName("role_name");
        searchBoParam.setOperator(Operator.EQ);
        searchBoParam.setValue("admin");
        var params = List.of(searchBoParam);
        var searchBo = new SearchBo(params, 1, 5, Sort.Direction.ASC, "id");

        var result = webTestClient.post().uri("/adminRole/query")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(searchBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminRoleBoListRes.class);

        log.info("/adminRole/query:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    @Test
    public void findOne() {
        IdLongReq idLongReq = new IdLongReq();
        idLongReq.setId(1L);

        var result = webTestClient.post().uri("/adminRole/findOne")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminRoleBoRes.class);

        log.info("/adminRole/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    @Test
    public void softDelete() {
        IdsLongReq idsLongReq = new IdsLongReq();
        idsLongReq.setIds(List.of(207174297410600960L, 207180606402985984L));

        var result = webTestClient.post().uri("/adminRole/softDelete")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/adminRole/softDelete:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    @Test
    public void findAllMenuTree() {

        var result = webTestClient.post().uri("/adminRole/findAllMenuTree")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminMenuButtonBoListRes.class);

        log.info("/adminRole/findAllMenuTree:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    @Test
    public void findAllByAdminRoleId() {
        IdLongReq idLongReq = new IdLongReq();
        idLongReq.setId(2L);

        var result = webTestClient.post().uri("/adminRole/findAllByAdminRoleId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminMenuButtonBoListRes.class);

        log.info("/adminRole/findAllMenuTree:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


}
