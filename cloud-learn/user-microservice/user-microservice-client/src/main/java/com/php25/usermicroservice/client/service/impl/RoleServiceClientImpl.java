package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.web.ReqIdLong;
import com.php25.common.flux.web.ReqIdsLong;
import com.php25.usermicroservice.client.dto.req.ReqSearchDto;
import com.php25.usermicroservice.client.dto.res.ResAdminMenuButtonDtoList;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.dto.res.ResRoleDto;
import com.php25.usermicroservice.client.dto.res.ResRoleDtoList;
import com.php25.usermicroservice.client.dto.res.RoleDto;
import com.php25.usermicroservice.client.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: penghuiping
 * @date: 2019/7/15 19:40
 * @description:
 */
@Component
public class RoleServiceClientImpl implements RoleService {

    @Autowired
    @Qualifier("Userservice_UserWebClient")
    private WebClient webClient;

    @Override
    public Mono<ResRoleDtoList> query(ReqSearchDto searchDto) {
        return webClient
                .post()
                .uri("/adminRole/query")
                .syncBody(searchDto)
                .retrieve()
                .bodyToMono(ResRoleDtoList.class);
    }


    @Override
    public Mono<ResRoleDto> save(RoleDto adminRoleDto) {
        return webClient
                .post()
                .uri("/adminRole/save")
                .syncBody(adminRoleDto)
                .retrieve()
                .bodyToMono(ResRoleDto.class);
    }


    @Override
    public Mono<ResBoolean> softDelete(ReqIdsLong idsLongReq) {
        return webClient
                .post()
                .uri("/adminRole/softDelete")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResRoleDto> findOne(@Valid ReqIdLong idLongReq) {
        return webClient
                .post()
                .uri("/adminRole/findOne")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(ResRoleDto.class);
    }

    @Override
    public Mono<ResAdminMenuButtonDtoList> findAllMenuTree() {
        return webClient
                .get()
                .uri("/adminRole/findAllMenuTree")
                .retrieve()
                .bodyToMono(ResAdminMenuButtonDtoList.class);
    }


    @Override
    public Mono<ResAdminMenuButtonDtoList> findAllByAdminRoleId(ReqIdLong idLongReq) {
        return webClient
                .post()
                .uri("/adminRole/findAllByAdminRoleId")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(ResAdminMenuButtonDtoList.class);
    }
}
