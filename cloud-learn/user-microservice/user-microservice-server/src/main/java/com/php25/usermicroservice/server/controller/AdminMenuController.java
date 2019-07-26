package com.php25.usermicroservice.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.common.redis.RedisService;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.HasRightAccessUrlBo;
import com.php25.usermicroservice.client.bo.res.AdminMenuButtonBoListRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.rpc.AdminMenuRpc;
import com.php25.usermicroservice.server.constant.RedisConstant;
import com.php25.usermicroservice.server.model.AdminAuthority;
import com.php25.usermicroservice.server.model.AdminAuthorityRef;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminMenuButtonRef;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.model.AdminRoleRef;
import com.php25.usermicroservice.server.model.AdminUser;
import com.php25.usermicroservice.server.repository.AdminAuthorityRepository;
import com.php25.usermicroservice.server.repository.AdminMenuButtonRepository;
import com.php25.usermicroservice.server.repository.AdminRoleRepository;
import com.php25.usermicroservice.server.repository.AdminUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminMenu")
public class AdminMenuController implements AdminMenuRpc {

    @Autowired
    private AdminMenuButtonRepository adminMenuButtonRepository;

    @Autowired
    private AdminAuthorityRepository adminAuthorityRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private RedisService redisService;

    @Override
    @PostMapping("/findAllMenuTree")
    public Mono<AdminMenuButtonBoListRes> findAllMenuTree() {
        return Mono.fromCallable(() -> {
            Optional<List<AdminMenuButton>> optionalAdminMenuButtons = adminMenuButtonRepository.findRootMenusEnabled();
            if (optionalAdminMenuButtons.isPresent() && !optionalAdminMenuButtons.get().isEmpty()) {
                List<AdminMenuButton> adminMenuButtons = optionalAdminMenuButtons.get();
                return adminMenuButtons.stream().map(adminMenuButton -> {
                    AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                    return adminMenuButtonBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminMenuButtonBo>();
            }
        }).map(adminMenuButtonBos -> {
            AdminMenuButtonBoListRes adminMenuButtonBoListRes = new AdminMenuButtonBoListRes();
            adminMenuButtonBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminMenuButtonBoListRes.setReturnObject(adminMenuButtonBos);
            return adminMenuButtonBoListRes;
        });
    }

    @Override
    @PostMapping("/hasRightAccessUrl")
    public Mono<BooleanRes> hasRightAccessUrl(@RequestBody Mono<HasRightAccessUrlBo> hasRightAccessUrlBoMono) {
        return hasRightAccessUrlBoMono.map(hasRightAccessUrlBo -> {
            Long adminUserId = hasRightAccessUrlBo.getAdminUserId();
            String url = hasRightAccessUrlBo.getUrl();
            Optional<AdminUser> adminUserDtoOptional = adminUserRepository.findById(adminUserId);
            if (!adminUserDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException("can't find a record of AdminUser in database," +
                        "please check the correctness of the 'adminUserId' parameter ");
            }

            //从redis中获取缓存
            Set<AdminAuthority> adminAuthorityDtos = redisService.get(RedisConstant.ADMIN_USER_AUTH + adminUserId, new TypeReference<Set<AdminAuthority>>() {
            });

            if (null != adminAuthorityDtos && !adminAuthorityDtos.isEmpty()) {
                Optional<AdminAuthority> adminAuthorityDtoOptional = adminAuthorityDtos.stream()
                        .filter(adminAuthorityDto -> adminAuthorityDto.equals(url)).findAny();

                if (adminAuthorityDtoOptional.isPresent()) {
                    return true;
                } else {
                    return false;
                }
            }

            //查询后台管理用户信息
            AdminUser adminUser = adminUserDtoOptional.get();
            Set<AdminRoleRef> adminRoleRefs = adminUser.getRoles();
            if (null == adminRoleRefs || adminRoleRefs.isEmpty()) {
                throw Exceptions.throwIllegalStateException(String.format("The adminUser object whose id is %d doesn't has a role?This can't happen!", adminUserId));
            }

            //接着查询后台管理用户对应的角色信息
            List<Long> adminRoleIds = adminRoleRefs.stream().map(AdminRoleRef::getRoleId).collect(Collectors.toList());
            Iterable<AdminRole> adminRoles = adminRoleRepository.findAllById(adminRoleIds);
            List<AdminRole> adminRoleList = Lists.newArrayList(adminRoles);
            if (null != adminRoleList && adminRoleList.size() > 0) {
                throw Exceptions.throwImpossibleException();
            }

            //找出权限集合
            Set<Long> adminAuthorityIdSet = new HashSet<>();

            adminRoleList.forEach(adminRole -> {
                Set<AdminAuthorityRef> adminAuthorityRefs = adminRole.getAdminAuthorities();
                Set<Long> tmp1 = adminAuthorityRefs.stream()
                        .map(AdminAuthorityRef::getAuthorityId)
                        .collect(Collectors.toSet());
                adminAuthorityIdSet.addAll(tmp1);
            });

            Iterable<AdminAuthority> adminAuthorities = adminAuthorityRepository.findAllById(Lists.newArrayList(adminAuthorityIdSet));
            List<AdminAuthority> adminAuthorityList = Lists.newArrayList(adminAuthorities);
            if (null != adminAuthorityList || !adminAuthorityList.isEmpty()) {
                throw Exceptions.throwImpossibleException();
            }

            //判断是否存在这个权限
            Optional<AdminAuthority> adminAuthorityOptional = adminAuthorityList.stream()
                    .filter(adminAuthority -> adminAuthority.equals(url)).findAny();

            if (adminAuthorityOptional.isPresent()) {
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
    @PostMapping("/findAllByAdminRoleId")
    public Mono<AdminMenuButtonBoListRes> findAllByAdminRoleId(@RequestBody Mono<IdLongReq> roleIdMono) {
        //参数验证
        return roleIdMono.map(idLongReq -> {
            Long roleId = idLongReq.getId();
            Optional<AdminRole> adminRoleOptional = adminRoleRepository.findById(roleId);
            if (!adminRoleOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("can't find a role record in database by roleId:%d", roleId));
            }
            AdminRole adminRole = adminRoleOptional.get();

            Set<AdminMenuButtonRef> list = adminRole.getAdminMenuButtons();

            if (null != list && !list.isEmpty()) {
                List<Long> adminMenuIds = list.stream().map(AdminMenuButtonRef::getMenuId).collect(Collectors.toList());
                Iterable<AdminMenuButton> adminMenuButtons = adminMenuButtonRepository.findAllById(adminMenuIds);
                return Lists.newArrayList(adminMenuButtons).stream().map(adminMenuButton -> {
                    AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                    return adminMenuButtonBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminMenuButtonBo>();
            }
        }).map(adminMenuButtonBos -> {
            AdminMenuButtonBoListRes adminMenuButtonBoListRes = new AdminMenuButtonBoListRes();
            adminMenuButtonBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminMenuButtonBoListRes.setReturnObject(adminMenuButtonBos);
            return adminMenuButtonBoListRes;
        });
    }
}
