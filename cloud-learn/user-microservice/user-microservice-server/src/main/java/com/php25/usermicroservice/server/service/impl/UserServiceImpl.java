package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.ReqIdLong;
import com.php25.common.flux.web.ReqIdsLong;
import com.php25.usermicroservice.client.dto.req.ReqChangePasswordDto;
import com.php25.usermicroservice.client.dto.req.ReqLoginDto;
import com.php25.usermicroservice.client.dto.req.ReqSearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDtoList;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.dto.res.RoleDto;
import com.php25.usermicroservice.client.service.UserService;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.model.RoleRef;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
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
@RequestMapping("/user")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @PostMapping("/login")
    public Mono<ResAdminUserDto> login(@RequestBody ReqLoginDto loginDto) {
        return Mono.just(loginDto).map(loginBo -> {
            Optional<User> adminUserOptional = userRepository.findByUsernameAndPassword(loginBo.getUsername(), loginBo.getPassword());
            if (!adminUserOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException("无法通过username:" + loginBo.getUsername() + ",password:" + loginBo.getPassword() + "找到用户信息");
            } else {
                User adminUser = adminUserOptional.get();
                AdminUserDto adminUserBo = new AdminUserDto();
                BeanUtils.copyProperties(adminUser, adminUserBo);

                if (null != adminUser.getRoles() && adminUser.getRoles().size() > 0) {
                    //加入角色信息
                    var ids = adminUser.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
                    Iterable<Role> adminRoles = roleRepository.findAllById(ids);
                    var adminRoleBos = Lists.newArrayList(adminRoles).stream().map(adminRole -> {
                        RoleDto adminRoleBo = new RoleDto();
                        BeanUtils.copyProperties(adminRole, adminRoleBo);
                        return adminRoleBo;
                    }).collect(Collectors.toList());
                    adminUserBo.setRoles(adminRoleBos);
                }
                return adminUserBo;
            }
        }).map(adminUserBo -> {
            ResAdminUserDto adminUserBoRes = new ResAdminUserDto();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/resetPassword")
    public Mono<ResBoolean> resetPassword(@RequestBody ReqIdsLong idsLongReq1) {
        //初始化密码为123456
        return Mono.just(idsLongReq1).map(idsLongReq ->
                userRepository.updatePassword("123456", idsLongReq.getIds()))
                .map(aBoolean -> {
                    ResBoolean booleanRes = new ResBoolean();
                    booleanRes.setErrorCode(ApiErrorCode.ok.value);
                    booleanRes.setReturnObject(aBoolean);
                    return booleanRes;
                });
    }

    @Override
    @PostMapping("/changePassword")
    public Mono<ResBoolean> changePassword(@RequestBody ReqChangePasswordDto changePasswordDto) {
        return Mono.just(changePasswordDto).map(changePasswordBo -> {
            Optional<User> adminUserOptional = userRepository.findById(changePasswordBo.getAdminUserId());
            if (!adminUserOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", changePasswordBo.getAdminUserId()));
            }

            User adminUser = adminUserOptional.get();
            if (!adminUser.getPassword().equals(changePasswordBo.getOriginPassword())) {
                throw Exceptions.throwIllegalStateException(String.format("originPassword:%s与数据库的密码不一样", changePasswordBo.getOriginPassword()));
            }
            adminUser.setPassword(changePasswordBo.getNewPassword());
            User adminUser1 = userRepository.save(adminUser);
            if (null != adminUser1) {
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            ResBoolean booleanRes = new ResBoolean();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping(value = "/findOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResAdminUserDto> findOne(@RequestBody ReqIdLong idLongReq1) {
        return Mono.just(idLongReq1).map(idLongReq -> {
            Optional<User> adminUserOptional = userRepository.findById(idLongReq.getId());
            if (adminUserOptional.isPresent()) {
                User adminUser = adminUserOptional.get();
                AdminUserDto adminUserBo = new AdminUserDto();
                BeanUtils.copyProperties(adminUser, adminUserBo);

                //加入角色信息
                var ids = adminUser.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
                Iterable<Role> adminRoles = roleRepository.findAllById(ids);
                var adminRoleBos = Lists.newArrayList(adminRoles).stream().map(adminRole -> {
                    RoleDto adminRoleBo = new RoleDto();
                    BeanUtils.copyProperties(adminRole, adminRoleBo);
                    return adminRoleBo;
                }).collect(Collectors.toList());
                adminUserBo.setRoles(adminRoleBos);

                return adminUserBo;
            } else {
                throw Exceptions.throwIllegalStateException("无法通过id:" + idLongReq.toString() + "找到对应的后台用户");
            }
        }).map(adminUserBo -> {
            ResAdminUserDto adminUserBoRes = new ResAdminUserDto();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<ResAdminUserDto> save(@RequestBody AdminUserDto adminUserDto) {
        return Mono.just(adminUserDto).map(adminUserBo -> {
            User adminUser = new User();
            BeanUtils.copyProperties(adminUserBo, adminUser);
            //是否有角色
            List<RoleDto> adminRoleBoList = adminUserBo.getRoles();
            if (null != adminRoleBoList && !adminRoleBoList.isEmpty()) {
                //处理角色
                Set<RoleRef> adminRoleRefs = adminRoleBoList.stream().map(adminRoleBo -> {
                    RoleRef adminRoleRef = new RoleRef();
                    adminRoleRef.setRoleId(adminRoleBo.getId());
                    return adminRoleRef;
                }).collect(Collectors.toSet());
                adminUser.setRoles(adminRoleRefs);
            }

            User adminUser1 = userRepository.save(adminUser);
            if (null != adminUser1) {
                adminUserBo.setId(adminUser1.getId());
                return adminUserBo;
            } else {
                throw Exceptions.throwIllegalStateException("保存用户信息失败:" + JsonUtil.toJson(adminUserBo));
            }
        }).map(adminUserBo -> {
            ResAdminUserDto adminUserBoRes = new ResAdminUserDto();
            adminUserBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoRes.setReturnObject(adminUserBo);
            return adminUserBoRes;
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<ResBoolean> softDelete(@RequestBody ReqIdsLong idsLongReq1) {
        return Mono.just(idsLongReq1).map(idsLongReq -> {
            Iterable<User> adminUsers = userRepository.findAllById(idsLongReq.getIds());
            var list = Lists.newArrayList(adminUsers).stream().map(User::getId).collect(Collectors.toList());
            if (null != list && list.size() > 0) {
                userRepository.softDelete(list);
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            ResBoolean booleanRes = new ResBoolean();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/query")
    public Mono<ResAdminUserDtoList> query(@RequestBody ReqSearchDto searchDto) {
        return Mono.just(searchDto).map(searchBo -> {
            var searchParams = searchBo.getSearchParams().stream()
                    .map(searchBoParam -> SearchParam.of(searchBoParam.getFieldName(), searchBoParam.getOperator(), searchBoParam.getValue())).collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
            var sort = Sort.by(searchBo.getDirection(), searchBo.getProperty());
            var page = PageRequest.of(searchBo.getPageNum(), searchBo.getPageSize(), sort);
            Page<User> adminUserPage = userRepository.findAll(searchParamBuilder, page);
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
            ResAdminUserDtoList adminUserBoListRes = new ResAdminUserDtoList();
            adminUserBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminUserBoListRes.setReturnObject(adminUserBos);
            return adminUserBoListRes;
        });
    }


}
