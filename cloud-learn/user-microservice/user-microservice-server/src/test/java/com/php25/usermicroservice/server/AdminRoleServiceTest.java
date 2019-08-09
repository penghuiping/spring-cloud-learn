package com.php25.usermicroservice.server;

import com.php25.common.core.specification.Operator;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.IdLongReq;
import com.php25.common.flux.web.IdsLongReq;
import com.php25.usermicroservice.client.dto.AdminMenuButtonDto;
import com.php25.usermicroservice.client.dto.AdminRoleDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.SearchDtoParam;
import com.php25.usermicroservice.client.dto.res.AdminMenuButtonDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "development")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = UserServiceApplication.class)
public class AdminRoleServiceTest {

    private WebTestClient webTestClient;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private ReactiveWebApplicationContext context;

    @Before
    public void setUp() {
        this.webTestClient = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .filter(documentationConfiguration(this.restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    //    @Test
    public void save() {
        AdminRoleDto adminRoleBo = new AdminRoleDto();
        adminRoleBo.setName("admin1");
        adminRoleBo.setDescription("管理员1");
        adminRoleBo.setCreateTime(LocalDateTime.now());
        adminRoleBo.setUpdateTime(LocalDateTime.now());
        adminRoleBo.setEnable(1);

        AdminMenuButtonDto adminMenuButtonBo = new AdminMenuButtonDto();
        adminMenuButtonBo.setId(1L);
        AdminMenuButtonDto adminMenuButtonBo1 = new AdminMenuButtonDto();
        adminMenuButtonBo1.setId(2L);
        AdminMenuButtonDto adminMenuButtonBo3 = new AdminMenuButtonDto();
        adminMenuButtonBo3.setId(3L);
        adminRoleBo.setMenus(List.of(adminMenuButtonBo, adminMenuButtonBo1, adminMenuButtonBo3));


        var result = webTestClient.post().uri("/adminRole/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(adminRoleBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminRoleDtoRes.class);

        log.info("/adminRole/save:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));

    }


    @Test
    public void query() {
        SearchDtoParam searchBoParam = new SearchDtoParam();
        searchBoParam.setFieldName("role_name");
        searchBoParam.setOperator(Operator.EQ);
        searchBoParam.setValue("admin");
        List params = List.of(searchBoParam);
        SearchDto searchBo = new SearchDto(params, 1, 5, Sort.Direction.ASC, "id");
        log.info("参数为:{}", JsonUtil.toJson(searchBo));
        var result = webTestClient.post().uri("/adminRole/query")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(searchBo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminRoleDtoListRes.class).consumeWith(document("adminRole", requestFields(
                        fieldWithPath("pageNum").description("当前第几页"),
                        fieldWithPath("pageSize").description("页面数量"),
                        fieldWithPath("direction").description("排序属性方向"),
                        fieldWithPath("property").description("排序属性"),
                        fieldWithPath("searchParams").description("搜索参数")
                ).andWithPrefix("searchParams[].",
                        fieldWithPath("fieldName").description("属性名"),
                        fieldWithPath("value").description("属性值"),
                        fieldWithPath("operator").description("比较符号")
                ), responseFields(beneathPath("returnObject"),
//                        fieldWithPath("errorCode").description("错误码"),
//                        fieldWithPath("message").description("错误信息"),
//                        fieldWithPath("returnObject").description("角色列表")).andWithPrefix("returnObject[].",
                        fieldWithPath("id").description("角色id"),
                        fieldWithPath("name").description("角色名"),
                        fieldWithPath("description").description("角色描述"),
                        fieldWithPath("createTime").description("角色创建时间"),
                        fieldWithPath("updateTime").description("角色更新时间"),
                        fieldWithPath("menus").description("菜单项"),
                        fieldWithPath("enable").description("是否有效"))));
        log.info("/adminRole/query:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    //    @Test
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
                .expectBody(AdminRoleDtoRes.class);

        log.info("/adminRole/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }

    //    @Test
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


    //    @Test
    public void findAllMenuTree() {

        var result = webTestClient.post().uri("/adminRole/findAllMenuTree")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AdminMenuButtonDtoListRes.class);

        log.info("/adminRole/findAllMenuTree:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


    //    @Test
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
                .expectBody(AdminMenuButtonDtoListRes.class);

        log.info("/adminRole/findAllMenuTree:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
    }


}
