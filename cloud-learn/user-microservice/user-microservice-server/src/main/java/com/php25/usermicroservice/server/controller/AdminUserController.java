package com.php25.usermicroservice.server.controller;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminUserBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminUserBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import com.php25.usermicroservice.server.dto.AdminUserDto;
import com.php25.usermicroservice.server.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<AdminUserBoRes> login(@RequestBody Mono<LoginBo> loginBoMono) {
        return loginBoMono.map(loginBo -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword(loginBo.getUsername(), loginBo.getPassword());
            if (!adminUserDtoOptional.isPresent()) {
                throw Exceptions.throwServiceException("无法通过username:" + loginBo.getUsername() + ",password:" + loginBo.getPassword() + "找到用户信息");
            } else {
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDtoOptional.get(), adminUserBo);
                return adminUserBo;
            }
        }).map(adminUserBo -> {
            AdminUserBoRes adminUserBoRes = new AdminUserBoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/resetPassword")
    public Mono<BooleanRes> resetPassword(@RequestBody Mono<IdsLongReq> idsLongReqMono) {
        //初始化密码为123456
        return idsLongReqMono.map(idsLongReq ->
                adminUserService.updatePassword("123456", idsLongReq.getIds()))
                .map(aBoolean -> {
                    BooleanRes booleanRes = new BooleanRes();
                    booleanRes.setErrorCode(ApiErrorCode.ok.value);
                    booleanRes.setReturnObject(aBoolean);
                    return booleanRes;
                });
    }

    @Override
    @PostMapping("/changePassword")
    public Mono<BooleanRes> changePassword(@RequestBody Mono<ChangePasswordBo> changePasswordBoMono) {
        return changePasswordBoMono.map(changePasswordBo -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(changePasswordBo.getAdminUserId());
            if (!adminUserDtoOptional.isPresent()) {
                throw Exceptions.throwServiceException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", changePasswordBo.getAdminUserId()));
            }

            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            if (!adminUserDto.getPassword().equals(changePasswordBo.getOriginPassword())) {
                throw Exceptions.throwServiceException(String.format("originPassword:%s与数据库的密码不一样", changePasswordBo.getOriginPassword()));
            }
            adminUserDto.setPassword(changePasswordBo.getNewPassword());
            Optional<AdminUserDto> adminUserDtoOptional1 = adminUserService.save(adminUserDto);
            if (adminUserDtoOptional1.isPresent()) {
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping(value = "/findOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<AdminUserBoRes> findOne(@RequestBody Mono<IdLongReq> idLongReqMono) {
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
        }).map(adminUserBo -> {
            AdminUserBoRes adminUserBoRes = new AdminUserBoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminUserBoRes> save(@RequestBody Mono<AdminUserBo> adminUserBoMono) {
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
        }).map(adminUserBo -> {
            AdminUserBoRes adminUserBoRes = new AdminUserBoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<BooleanRes> softDelete(@RequestBody Mono<IdsLongReq> idsLongReqMono) {
        return idsLongReqMono.map(idsLongReq -> {
            Optional<List<AdminUserDto>> optionalAdminUserDtos = adminUserService.findAll(idsLongReq.getIds());
            if (optionalAdminUserDtos.isPresent() && !optionalAdminUserDtos.get().isEmpty()) {
                adminUserService.softDelete(optionalAdminUserDtos.get());
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/query")
    public Mono<AdminUserBoListRes> query(@RequestBody Mono<SearchBo> searchBoMono) {
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
        }).map(adminUserBos -> {
            AdminUserBoListRes adminUserBoListRes = new AdminUserBoListRes();
            adminUserBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoListRes.setReturnObject(adminUserBos);
            return adminUserBoListRes;
        });
    }


}
