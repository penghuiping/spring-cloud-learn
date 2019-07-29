package com.php25.usermicroservice.server.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.dto.AdminRoleDto;
import com.php25.usermicroservice.client.dto.AdminUserDto;
import com.php25.usermicroservice.client.dto.ChangePasswordDto;
import com.php25.usermicroservice.client.dto.LoginDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.service.AdminUserService;
import com.php25.usermicroservice.server.model.Role;
import com.php25.usermicroservice.server.model.RoleRef;
import com.php25.usermicroservice.server.model.User;
import com.php25.usermicroservice.server.repository.RoleRepository;
import com.php25.usermicroservice.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/2 15:17
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminUser")
public class UserServiceImpl implements AdminUserService {

    @Autowired
    private UserRepository adminUserRepository;

    @Autowired
    private RoleRepository adminRoleRepository;

    @Override
    @PostMapping("/login")
    public Mono<AdminUserDtoRes> login(@RequestBody LoginDto loginDto) {
        return Mono.just(loginDto).map(loginBo -> {
            Optional<User> adminUserOptional = adminUserRepository.findByUsernameAndPassword(loginBo.getUsername(), loginBo.getPassword());
            if (!adminUserOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException("无法通过username:" + loginBo.getUsername() + ",password:" + loginBo.getPassword() + "找到用户信息");
            } else {
                User adminUser = adminUserOptional.get();
                AdminUserDto adminUserBo = new AdminUserDto();
                BeanUtils.copyProperties(adminUser, adminUserBo);

                if (null != adminUser.getRoles() && adminUser.getRoles().size() > 0) {
                    //加入角色信息
                    var ids = adminUser.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
                    Iterable<Role> adminRoles = adminRoleRepository.findAllById(ids);
                    var adminRoleBos = Lists.newArrayList(adminRoles).stream().map(adminRole -> {
                        AdminRoleDto adminRoleBo = new AdminRoleDto();
                        BeanUtils.copyProperties(adminRole, adminRoleBo);
                        return adminRoleBo;
                    }).collect(Collectors.toList());
                    adminUserBo.setRoles(adminRoleBos);
                }
                return adminUserBo;
            }
        }).map(adminUserBo -> {
            AdminUserDtoRes adminUserBoRes = new AdminUserDtoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/resetPassword")
    public Mono<BooleanRes> resetPassword(@RequestBody IdsLongReq idsLongReq1) {
        //初始化密码为123456
        return Mono.just(idsLongReq1).map(idsLongReq ->
                adminUserRepository.updatePassword("123456", idsLongReq.getIds()))
                .map(aBoolean -> {
                    BooleanRes booleanRes = new BooleanRes();
                    booleanRes.setErrorCode(ApiErrorCode.ok.value);
                    booleanRes.setReturnObject(aBoolean);
                    return booleanRes;
                });
    }

    @Override
    @PostMapping("/changePassword")
    public Mono<BooleanRes> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return Mono.just(changePasswordDto).map(changePasswordBo -> {
            Optional<User> adminUserOptional = adminUserRepository.findById(changePasswordBo.getAdminUserId());
            if (!adminUserOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", changePasswordBo.getAdminUserId()));
            }

            User adminUser = adminUserOptional.get();
            if (!adminUser.getPassword().equals(changePasswordBo.getOriginPassword())) {
                throw Exceptions.throwIllegalStateException(String.format("originPassword:%s与数据库的密码不一样", changePasswordBo.getOriginPassword()));
            }
            adminUser.setPassword(changePasswordBo.getNewPassword());
            User adminUser1 = adminUserRepository.save(adminUser);
            if (null != adminUser1) {
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
    public Mono<AdminUserDtoRes> findOne(@RequestBody IdLongReq idLongReq1) {
        return Mono.just(idLongReq1).map(idLongReq -> {
            Optional<User> adminUserOptional = adminUserRepository.findById(idLongReq.getId());
            if (adminUserOptional.isPresent()) {
                User adminUser = adminUserOptional.get();
                AdminUserDto adminUserBo = new AdminUserDto();
                BeanUtils.copyProperties(adminUser, adminUserBo);

                //加入角色信息
                var ids = adminUser.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
                Iterable<Role> adminRoles = adminRoleRepository.findAllById(ids);
                var adminRoleBos = Lists.newArrayList(adminRoles).stream().map(adminRole -> {
                    AdminRoleDto adminRoleBo = new AdminRoleDto();
                    BeanUtils.copyProperties(adminRole, adminRoleBo);
                    return adminRoleBo;
                }).collect(Collectors.toList());
                adminUserBo.setRoles(adminRoleBos);

                return adminUserBo;
            } else {
                throw Exceptions.throwIllegalStateException("无法通过id:" + idLongReq.toString() + "找到对应的后台用户");
            }
        }).map(adminUserBo -> {
            AdminUserDtoRes adminUserBoRes = new AdminUserDtoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminUserDtoRes> save(@RequestBody AdminUserDto adminUserDto) {
        return Mono.just(adminUserDto).map(adminUserBo -> {
            User adminUser = new User();
            BeanUtils.copyProperties(adminUserBo, adminUser);
            //是否有角色
            List<AdminRoleDto> adminRoleBoList = adminUserBo.getRoles();
            if (null != adminRoleBoList && !adminRoleBoList.isEmpty()) {
                //处理角色
                Set<RoleRef> adminRoleRefs = adminRoleBoList.stream().map(adminRoleBo -> {
                    RoleRef adminRoleRef = new RoleRef();
                    adminRoleRef.setRoleId(adminRoleBo.getId());
                    return adminRoleRef;
                }).collect(Collectors.toSet());
                adminUser.setRoles(adminRoleRefs);
            }

            User adminUser1 = adminUserRepository.save(adminUser);
            if (null != adminUser1) {
                adminUserBo.setId(adminUser1.getId());
                return adminUserBo;
            } else {
                throw Exceptions.throwIllegalStateException("保存用户信息失败:" + JsonUtil.toJson(adminUserBo));
            }
        }).map(adminUserBo -> {
            AdminUserDtoRes adminUserBoRes = new AdminUserDtoRes();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<BooleanRes> softDelete(@RequestBody IdsLongReq idsLongReq1) {
        return Mono.just(idsLongReq1).map(idsLongReq -> {
            Iterable<User> adminUsers = adminUserRepository.findAllById(idsLongReq.getIds());
            var list = Lists.newArrayList(adminUsers).stream().map(User::getId).collect(Collectors.toList());
            if (null != list && list.size() > 0) {
                adminUserRepository.softDelete(list);
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
    public Mono<AdminUserDtoListRes> query(@RequestBody SearchDto searchDto) {
        return Mono.just(searchDto).map(searchBo -> {
            var searchParams = searchBo.getSearchParams().stream()
                    .map(searchBoParam -> SearchParam.of(searchBoParam.getFieldName(), searchBoParam.getOperator(), searchBoParam.getValue())).collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
            var sort = Sort.by(searchBo.getDirection(), searchBo.getProperty());
            var page = PageRequest.of(searchBo.getPageNum(), searchBo.getPageSize(), sort);
            Page<User> adminUserPage = adminUserRepository.findAll(searchParamBuilder, page);
            if (null != adminUserPage && adminUserPage.getTotalElements() > 0) {
                return adminUserPage.stream().map(adminUserDto -> {
                    AdminUserDto adminUserBo = new AdminUserDto();
                    BeanUtils.copyProperties(adminUserDto, adminUserBo);
                    return adminUserBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminUserDto>();
            }
        }).map(adminUserBos -> {
            AdminUserDtoListRes adminUserBoListRes = new AdminUserDtoListRes();
            adminUserBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoListRes.setReturnObject(adminUserBos);
            return adminUserBoListRes;
        });
    }


}
