package com.php25.usermicroservice.server.controller;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.exception.ServiceException;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import com.php25.usermicroservice.server.dto.AdminUserDto;
import com.php25.usermicroservice.server.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/2 15:17
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminUser")
public class AdminUserController implements AdminUserRpc {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    @PostMapping("/login")
    public Mono<AdminUserBo> login(@Valid Mono<LoginBo> loginBoMono) {
        return loginBoMono.map(loginBo -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword(loginBo.getUsername(), loginBo.getPassword());
            if (!adminUserDtoOptional.isPresent()) {
                throw Exceptions.throwServiceException("无法通过username:" + loginBo.getUsername() + ",password:" + loginBo.getPassword() + "找到用户信息");
            } else {
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDtoOptional.get(), adminUserBo);
                return adminUserBo;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/resetPassword")
    public Mono<Boolean> resetPassword(@Valid Mono<IdsLongReq> idsLongReqMono) {
        //初始化密码为123456
        return idsLongReqMono.map(idsLongReq ->
                adminUserService.updatePassword("123456", idsLongReq.getIds()))
                .doOnError(throwable -> {
                    log.error("出错啦", throwable);
                });
    }

    @Override
    @PostMapping("/changePassword")
    public Mono<Boolean> changePassword(@Valid Mono<ChangePasswordBo> changePasswordBoMono) {
        return changePasswordBoMono.map(changePasswordBo -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(changePasswordBo.getAdminUserId());
            if (!adminUserDtoOptional.isPresent()) {
                throw new ServiceException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", changePasswordBo.getAdminUserId()));
            }

            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            if (!adminUserDto.getPassword().equals(changePasswordBo.getOriginPassword())) {
                throw new ServiceException(String.format("originPassword:%s与数据库的密码不一样", changePasswordBo.getOriginPassword()));
            }

            adminUserDto.setPassword(changePasswordBo.getNewPassword());
            Optional<AdminUserDto> adminUserDtoOptional1 = adminUserService.save(adminUserDto);
            if (adminUserDtoOptional1.isPresent()) {
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping(value = "/findOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<AdminUserBo> findOne(@Valid Mono<IdLongReq> idLongReqMono) {
        return idLongReqMono.map(idLongReq -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(idLongReq.getId());
            if (adminUserDtoOptional.isPresent()) {
                AdminUserDto adminUserDto = adminUserDtoOptional.get();
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDto, adminUserBo);
                return adminUserBo;
            } else {
                throw Exceptions.throwServiceException("无法通过id:" + idLongReq.toString() + "找到对应的后台用户");
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminUserBo> save(@Valid Mono<AdminUserBo> adminUserBoMono) {
        return adminUserBoMono.map(adminUserBo -> {
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUserBo, adminUserDto);
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.save(adminUserDto);
            if (adminUserDtoOptional.isPresent()) {
                adminUserBo.setId(adminUserDtoOptional.get().getId());
                return adminUserBo;
            } else {
                throw Exceptions.throwServiceException("保存用户信息失败:" + JsonUtil.toJson(adminUserBo));
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<Boolean> softDelete(Mono<IdsLongReq> idsLongReqMono) {
        return idsLongReqMono.map(idsLongReq -> {
            Optional<List<AdminUserDto>> optionalAdminUserDtos = adminUserService.findAll(idsLongReq.getIds());
            if (optionalAdminUserDtos.isPresent() && !optionalAdminUserDtos.get().isEmpty()) {
                adminUserService.softDelete(optionalAdminUserDtos.get());
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/query")
    public Flux<AdminUserBo> query(@Valid Mono<SearchBo> searchBoMono) {
        return searchBoMono.map(searchBo -> {
            var searchParams = searchBo.getSearchParams().stream()
                    .map(searchBoParam -> SearchParam.of(searchBoParam.getFieldName(), searchBoParam.getOperator(), searchBoParam.getValue())).collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
            var optionalAdminUserDtoDataGridPageDto =
                    adminUserService.query(searchBo.getPageNum(), searchBo.getPageSize(), searchParamBuilder, BeanUtils::copyProperties, Sort.by(searchBo.getDirection(), searchBo.getProperty()));
            if (optionalAdminUserDtoDataGridPageDto.isPresent()) {
                DataGridPageDto<AdminUserDto> dtoDataGridPageDto = optionalAdminUserDtoDataGridPageDto.get();
                return dtoDataGridPageDto.getData().stream().map(adminUserDto -> {
                    AdminUserBo adminUserBo = new AdminUserBo();
                    BeanUtils.copyProperties(adminUserDto, adminUserBo);
                    return adminUserBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminUserBo>();
            }
        }).flatMapMany(Flux::fromIterable).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }


}
