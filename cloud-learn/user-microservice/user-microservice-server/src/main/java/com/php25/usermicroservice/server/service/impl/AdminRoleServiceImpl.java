package com.php25.usermicroservice.server.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.common.redis.RedisService;
import com.php25.usermicroservice.client.dto.AdminMenuButtonDto;
import com.php25.usermicroservice.client.dto.AdminRoleDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.res.AdminMenuButtonDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminRoleDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.service.RoleService;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminMenuButtonRef;
import com.php25.usermicroservice.server.model.Role;
import com.php25.usermicroservice.server.repository.AdminMenuButtonRepository;
import com.php25.usermicroservice.server.repository.RoleRepository;
import com.php25.usermicroservice.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminRole")
public class AdminRoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository adminRoleRepository;

    @Autowired
    private AdminMenuButtonRepository adminMenuButtonRepository;

    @Autowired
    private UserRepository adminUserRepository;

    @Autowired
    private RedisService redisService;

    @Override
    @PostMapping("/query")
    public Mono<AdminRoleDtoListRes> query(@RequestBody SearchDto searchDto) {
        return Mono.just(searchDto).map(searchBo -> {
            var params = searchBo.getSearchParams().stream()
                    .map(searchParam -> SearchParam.of(searchParam.getFieldName(), searchParam.getOperator(), searchParam.getValue()))
                    .collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(params);
            Pageable pageable = PageRequest.of(searchBo.getPageNum(), searchBo.getPageSize(), searchBo.getDirection(), searchBo.getProperty());
            Page<Role> adminRolePage = adminRoleRepository.findAll(searchParamBuilder, pageable);
            if (null != adminRolePage && adminRolePage.getTotalElements() > 0) {
                List<Role> adminRoleList = adminRolePage.getContent();
                if (null != adminRoleList && adminRoleList.size() > 0) {
                    return adminRoleList.stream().map(adminRoleDto -> {
                        AdminRoleDto adminRoleBo = new AdminRoleDto();
                        BeanUtils.copyProperties(adminRoleDto, adminRoleBo);
                        return adminRoleBo;
                    }).collect(Collectors.toList());
                }
            }
            return new ArrayList<AdminRoleDto>();
        }).map(adminRoleBos -> {
            AdminRoleDtoListRes adminRoleBoListRes = new AdminRoleDtoListRes();
            adminRoleBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoListRes.setReturnObject(adminRoleBos);
            return adminRoleBoListRes;
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminRoleDtoRes> save(@RequestBody AdminRoleDto adminRoleDto) {
        log.info("....save");
        return Mono.just(adminRoleDto).map(adminRoleBo -> {
            Role adminRole = new Role();
            BeanUtils.copyProperties(adminRoleBo, adminRole);
            //判断是否有menus
            List<AdminMenuButtonDto> adminMenuButtonBos = adminRoleBo.getMenus();
            if (null != adminMenuButtonBos && !adminMenuButtonBos.isEmpty()) {
                //有的话就保存
                Set<AdminMenuButtonRef> adminMenuButtons = adminMenuButtonBos.stream().map(adminMenuButtonBo -> {
                    AdminMenuButtonRef adminMenuButtonRef = new AdminMenuButtonRef();
                    adminMenuButtonRef.setMenuId(adminMenuButtonBo.getId());
                    return adminMenuButtonRef;
                }).collect(Collectors.toSet());
                adminRole.setAdminMenuButtons(adminMenuButtons);
            }

            Role adminRole1 = adminRoleRepository.save(adminRole);
            if (null != adminRole1) {
                adminRoleBo.setId(adminRole1.getId());
                return adminRoleBo;
            } else {
                throw Exceptions.throwIllegalStateException("保存角色失败:" + JsonUtil.toJson(adminRoleBo));
            }
        }).map(adminRoleBo -> {
            AdminRoleDtoRes adminRoleBoRes = new AdminRoleDtoRes();
            adminRoleBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoRes.setReturnObject(adminRoleBo);
            return adminRoleBoRes;
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<AdminRoleDtoRes> findOne(@RequestBody IdLongReq idLongReq1) {
        return Mono.just(idLongReq1).map(idLongReq -> {
            Optional<Role> adminRoleOptional = adminRoleRepository.findById(idLongReq.getId());
            if (!adminRoleOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的角色记录", idLongReq.getId()));
            } else {
                Role adminRole = adminRoleOptional.get();
                AdminRoleDto adminRoleBo = new AdminRoleDto();
                BeanUtils.copyProperties(adminRole, adminRoleBo);

                //处理菜单与按钮
                Set<AdminMenuButtonRef> adminMenuButtonRefs = adminRole.getAdminMenuButtons();
                if (null != adminMenuButtonRefs && !adminMenuButtonRefs.isEmpty()) {
                    List<Long> menuIds = adminMenuButtonRefs.stream().map(AdminMenuButtonRef::getMenuId)
                            .collect(Collectors.toList());
                    Iterable<AdminMenuButton> adminMenuButtons = adminMenuButtonRepository.findAllById(menuIds);
                    List<AdminMenuButtonDto> adminMenuButtonBos = Lists.newArrayList(adminMenuButtons).stream().map(adminMenuButton -> {
                        AdminMenuButtonDto adminMenuButtonBo = new AdminMenuButtonDto();
                        BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                        return adminMenuButtonBo;
                    }).collect(Collectors.toList());
                    adminRoleBo.setMenus(adminMenuButtonBos);
                }
                return adminRoleBo;
            }
        }).map(adminRoleBo -> {
            AdminRoleDtoRes adminRoleBoRes = new AdminRoleDtoRes();
            adminRoleBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoRes.setReturnObject(adminRoleBo);
            return adminRoleBoRes;
        });
    }

    @Override
    @PostMapping("/softDelete")
    public Mono<BooleanRes> softDelete(@RequestBody IdsLongReq idsLongReq1) {
        return Mono.just(idsLongReq1).map(idsLongReq -> {
            Iterable<Role> adminRoles = adminRoleRepository.findAllById(idsLongReq.getIds());
            List<Role> adminRoleList = Lists.newArrayList(adminRoles);
            if (null != adminRoleList && adminRoleList.size() > 0) {
                List<Long> ids = adminRoleList.stream().map(Role::getId).collect(Collectors.toList());
                adminRoleRepository.softDelete(ids);
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setReturnObject(aBoolean);
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/findAllMenuTree")
    public Mono<AdminMenuButtonDtoListRes> findAllMenuTree() {
        return Mono.fromCallable(() -> {
            Optional<List<AdminMenuButton>> optionalAdminMenuButtons = adminMenuButtonRepository.findAllEnable();
            if (optionalAdminMenuButtons.isPresent() && !optionalAdminMenuButtons.get().isEmpty()) {
                List<AdminMenuButton> adminMenuButtons = optionalAdminMenuButtons.get();
                return adminMenuButtons.stream().map(adminMenuButton -> {
                    AdminMenuButtonDto adminMenuButtonBo = new AdminMenuButtonDto();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                    return adminMenuButtonBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminMenuButtonDto>();
            }
        }).map(adminMenuButtonBos -> {
            AdminMenuButtonDtoListRes adminMenuButtonBoListRes = new AdminMenuButtonDtoListRes();
            adminMenuButtonBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminMenuButtonBoListRes.setReturnObject(adminMenuButtonBos);
            return adminMenuButtonBoListRes;
        });
    }


    @Override
    @PostMapping("/findAllByAdminRoleId")
    public Mono<AdminMenuButtonDtoListRes> findAllByAdminRoleId(@RequestBody IdLongReq idLongReq1) {
        //参数验证
        return Mono.just(idLongReq1).map(idLongReq -> {
            Long roleId = idLongReq.getId();
            Optional<Role> adminRoleOptional = adminRoleRepository.findById(roleId);
            if (!adminRoleOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("can't find a role record in database by roleId:%d", roleId));
            }
            Role adminRole = adminRoleOptional.get();

            Set<AdminMenuButtonRef> list = adminRole.getAdminMenuButtons();

            if (null != list && !list.isEmpty()) {
                List<Long> adminMenuIds = list.stream().map(AdminMenuButtonRef::getMenuId).collect(Collectors.toList());
                Iterable<AdminMenuButton> adminMenuButtons = adminMenuButtonRepository.findAllById(adminMenuIds);
                return Lists.newArrayList(adminMenuButtons).stream().map(adminMenuButton -> {
                    AdminMenuButtonDto adminMenuButtonBo = new AdminMenuButtonDto();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                    return adminMenuButtonBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminMenuButtonDto>();
            }
        }).map(adminMenuButtonBos -> {
            AdminMenuButtonDtoListRes adminMenuButtonBoListRes = new AdminMenuButtonDtoListRes();
            adminMenuButtonBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminMenuButtonBoListRes.setReturnObject(adminMenuButtonBos);
            return adminMenuButtonBoListRes;
        });
    }
}
