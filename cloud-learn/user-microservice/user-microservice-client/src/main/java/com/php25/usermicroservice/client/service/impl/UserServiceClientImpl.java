package com.php25.usermicroservice.client.service.impl;

import com.php25.common.flux.web.ReqIdLong;
import com.php25.common.flux.web.ReqIdsLong;
import com.php25.usermicroservice.client.dto.req.ReqChangePasswordDto;
import com.php25.usermicroservice.client.dto.req.ReqLoginDto;
import com.php25.usermicroservice.client.dto.req.ReqSearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDtoList;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/7/15 19:40
 * @description:
 */
@Component
public class UserServiceClientImpl implements UserService {

    @Autowired
    @Qualifier("Userservice_UserWebClient")
    private WebClient webClient;

    @Override
    public Mono<ResAdminUserDto> login(ReqLoginDto loginDto) {
        return webClient
                .post()
                .uri("/adminUser/login")
                .syncBody(loginDto)
                .retrieve()
                .bodyToMono(ResAdminUserDto.class);
    }

    @Override
    public Mono<ResBoolean> resetPassword(ReqIdsLong idsLongReq) {
        return webClient
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResBoolean> changePassword(ReqChangePasswordDto changePasswordDto) {
        return webClient
                .post()
                .uri("/adminUser/changePassword")
                .syncBody(changePasswordDto)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResAdminUserDto> findOne(ReqIdLong idLongReq) {
        return webClient
                .post()
                .uri("/adminUser/findOne/")
                .syncBody(idLongReq)
                .retrieve()
                .bodyToMono(ResAdminUserDto.class);
    }

    @Override
    public Mono<ResAdminUserDto> save(AdminUserDto adminUserDto) {
        return webClient
                .post()
                .uri("/adminUser/resetPassword")
                .syncBody(adminUserDto)
                .retrieve()
                .bodyToMono(ResAdminUserDto.class);
    }

    @Override
    public Mono<ResBoolean> softDelete(ReqIdsLong idsLongReq) {
        return webClient
                .post()
                .uri("/adminUser/softDelete")
                .syncBody(idsLongReq)
                .retrieve()
                .bodyToMono(ResBoolean.class);
    }

    @Override
    public Mono<ResAdminUserDtoList> query(ReqSearchDto searchDto) {
        return webClient
                .post()
                .uri("/adminUser/query")
                .syncBody(searchDto)
                .retrieve()
                .bodyToMono(ResAdminUserDtoList.class);
    }
}
