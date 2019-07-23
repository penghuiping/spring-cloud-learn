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
import com.php25.usermicroservice.server.dto.AdminAuthorityDto;
import com.php25.usermicroservice.server.dto.AdminMenuButtonDto;
import com.php25.usermicroservice.server.dto.AdminRoleDto;
import com.php25.usermicroservice.server.dto.AdminUserDto;
import com.php25.usermicroservice.server.service.AdminAuthorityService;
import com.php25.usermicroservice.server.service.AdminMenuService;
import com.php25.usermicroservice.server.service.AdminRoleService;
import com.php25.usermicroservice.server.service.AdminUserService;
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
    private AdminMenuService adminMenuService;

    @Autowired
    private AdminAuthorityService adminAuthorityService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RedisService redisService;

    @Override
    @PostMapping("/findAllMenuTree")
    public Mono<AdminMenuButtonBoListRes> findAllMenuTree() {
        return Mono.fromCallable(() -> {
            Optional<List<AdminMenuButtonDto>> optionalAdminMenuButtonDtos = adminMenuService.findRootMenusEnabled();
            if (optionalAdminMenuButtonDtos.isPresent() && !optionalAdminMenuButtonDtos.get().isEmpty()) {
                List<AdminMenuButtonDto> adminMenuButtonDtos = optionalAdminMenuButtonDtos.get();
                return adminMenuButtonDtos.stream().map(adminMenuButtonDto -> {
                    AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
                    BeanUtils.copyProperties(adminMenuButtonDto, adminMenuButtonBo);
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
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(adminUserId);
            if (!adminUserDtoOptional.isPresent()) {
                throw new IllegalArgumentException("can't find a record of AdminUser in database," +
                        "please check the correctness of the 'adminUserId' parameter ");
            }

            //从redis中获取缓存
            Set<AdminAuthorityDto> adminAuthorityDtos = redisService.get(RedisConstant.ADMIN_USER_AUTH + adminUserId, new TypeReference<Set<AdminAuthorityDto>>() {
            });

            if (null != adminAuthorityDtos && !adminAuthorityDtos.isEmpty()) {
                Optional<AdminAuthorityDto> adminAuthorityDtoOptional = adminAuthorityDtos.stream()
                        .filter(adminAuthorityDto -> adminAuthorityDto.equals(url)).findAny();

                if (adminAuthorityDtoOptional.isPresent()) {
                    return true;
                } else {
                    return false;
                }
            }

            //查询后台管理用户信息
            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            List<AdminRoleDto> adminRoleDtos = adminUserDto.getRoles();
            if (null == adminRoleDtos || adminRoleDtos.isEmpty()) {
                throw new RuntimeException(String.format("The adminUser object whose id is %d doesn't has a role?This can't happen!", adminUserId));
            }

            //接着查询后台管理用户对应的角色信息
            List<Long> adminRoleIds = adminRoleDtos.stream().map(adminRoleDto -> adminRoleDto.getId()).collect(Collectors.toList());
            Optional<List<AdminRoleDto>> optionalAdminRoleDtos = adminRoleService.findAll(adminRoleIds);

            if (!optionalAdminRoleDtos.isPresent()) {
                throw new RuntimeException("It can't happen");
            }
            List<AdminRoleDto> adminRoleDtos1 = optionalAdminRoleDtos.get();

            //找出权限集合
            Set<Long> adminMenuButtonIdSet = new HashSet<>();
            adminRoleDtos1.forEach(adminRoleDto -> {
                Optional<List<AdminMenuButtonDto>> adminMenuButtonDtoOptional = adminMenuService.findMenusEnabledByRole(adminRoleDto);
                if (adminMenuButtonDtoOptional.isPresent() && !adminMenuButtonDtoOptional.get().isEmpty()) {
                    List<AdminMenuButtonDto> adminMenuButtonDtos = adminMenuButtonDtoOptional.get();
                    adminMenuButtonDtos.forEach(adminMenuButtonDto -> {
                        adminMenuButtonIdSet.add(adminMenuButtonDto.getId());
                    });
                }
            });

            Optional<Set<AdminAuthorityDto>> optionalAdminAuthorityDtos = adminAuthorityService.findAllDistinctByAdminMenuButtonIds(Lists.newArrayList(adminMenuButtonIdSet));

            if (!optionalAdminAuthorityDtos.isPresent() || optionalAdminAuthorityDtos.get().isEmpty()) {
                throw new RuntimeException("It can't happen");
            }

            adminAuthorityDtos = optionalAdminAuthorityDtos.get();

            //判断是否存在这个权限
            Optional<AdminAuthorityDto> adminAuthorityDtoOptional = adminAuthorityDtos.stream()
                    .filter(adminAuthorityDto -> adminAuthorityDto.equals(url)).findAny();

            if (adminAuthorityDtoOptional.isPresent()) {
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
            Optional<AdminRoleDto> adminRoleDtoOptional = adminRoleService.findOne(roleId);
            if (!adminRoleDtoOptional.isPresent()) {
                throw Exceptions.throwServiceException(String.format("can't find a role record in database by roleId:%d", roleId));
            }
            AdminRoleDto adminRoleDto = adminRoleDtoOptional.get();
            Optional<List<AdminMenuButtonDto>> optionalAdminMenuButtonDtos = adminMenuService.findMenusEnabledByRole(adminRoleDto);
            if (optionalAdminMenuButtonDtos.isPresent() && !optionalAdminMenuButtonDtos.get().isEmpty()) {
                return optionalAdminMenuButtonDtos.get().stream().map(adminMenuButtonDto -> {
                    AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
                    BeanUtils.copyProperties(adminMenuButtonDto, adminMenuButtonBo);
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
