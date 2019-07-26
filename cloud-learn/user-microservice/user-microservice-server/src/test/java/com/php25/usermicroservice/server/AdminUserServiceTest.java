package com.php25.usermicroservice.server;

import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.SearchBoParam;
import com.php25.usermicroservice.client.bo.res.AdminUserBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminUserBoRes;
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
 * @date: 2018/10/12 09:54
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminUserServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Test
    public void save() {
        AdminUserBo adminUserBo = new AdminUserBo();
        adminUserBo.setNickname("mary");
        adminUserBo.setUsername("mary");
        adminUserBo.setEmail("123@qq.com");
        adminUserBo.setEnable(1);
        adminUserBo.setCreateTime(LocalDateTime.now());
        adminUserBo.setUpdateTime(LocalDateTime.now());
        adminUserBo.setMobile("18621287362");
        adminUserBo.setPassword("123456");

        AdminRoleBo adminRoleBo = new AdminRoleBo();
        adminRoleBo.setId(2L);
        adminUserBo.setRoles(List.of(adminRoleBo));

        var result = webTestClient.post().uri("/adminUser/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(adminUserBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminUserBoRes.class);

        log.info("/adminUser/save:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


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
    public void resetPassword() {
        IdsLongReq idsLongReq = new IdsLongReq();
        idsLongReq.setIds(List.of(1L));


        var result = webTestClient.post().uri("/adminUser/resetPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/adminUser/resetPassword:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    @Test
    public void changePassword() {
        ChangePasswordBo changePasswordBo = new ChangePasswordBo();
        changePasswordBo.setAdminUserId(1L);
        changePasswordBo.setNewPassword("654321");
        changePasswordBo.setOriginPassword("123456");

        var result = webTestClient.post().uri("/adminUser/changePassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(changePasswordBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/adminUser/changePassword:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    @Test
    public void findOne() {
        IdLongReq idsLongReq = new IdLongReq();
        idsLongReq.setId(1L);

        var result = webTestClient.post().uri("/adminUser/findOne")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminUserBoRes.class);

        log.info("/adminUser/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }




    @Test
    public void softDelete() {
        IdsLongReq idsLongReq = new IdsLongReq();
        idsLongReq.setIds(List.of(207156698513670144L));

        var result = webTestClient.post().uri("/adminUser/softDelete")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(idsLongReq)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(BooleanRes.class);

        log.info("/adminUser/softDelete:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
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