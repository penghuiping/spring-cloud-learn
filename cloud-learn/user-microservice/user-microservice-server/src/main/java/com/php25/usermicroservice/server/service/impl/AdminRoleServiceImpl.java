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
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminMenuButtonBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.rpc.RoleService;
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
    public Mono<AdminRoleBoListRes> query(@RequestBody Mono<SearchBo> searchBoMono) {
        return searchBoMono.map(searchBo -> {
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
                        AdminRoleBo adminRoleBo = new AdminRoleBo();
                        BeanUtils.copyProperties(adminRoleDto, adminRoleBo);
                        return adminRoleBo;
                    }).collect(Collectors.toList());
                }
            }
            return new ArrayList<AdminRoleBo>();
        }).map(adminRoleBos -> {
            AdminRoleBoListRes adminRoleBoListRes = new AdminRoleBoListRes();
            adminRoleBoListRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoListRes.setReturnObject(adminRoleBos);
            return adminRoleBoListRes;
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminRoleBoRes> save(@RequestBody Mono<AdminRoleBo> adminRoleBoMono) {
        log.info("....save");
        return adminRoleBoMono.map(adminRoleBo -> {
            Role adminRole = new Role();
            BeanUtils.copyProperties(adminRoleBo, adminRole);
            //判断是否有menus
            List<AdminMenuButtonBo> adminMenuButtonBos = adminRoleBo.getMenus();
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
            AdminRoleBoRes adminRoleBoRes = new AdminRoleBoRes();
            adminRoleBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoRes.setReturnObject(adminRoleBo);
            return adminRoleBoRes;
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<AdminRoleBoRes> findOne(@RequestBody Mono<IdLongReq> idLongReqMono) {
        return idLongReqMono.map(idLongReq -> {
            Optional<Role> adminRoleOptional = adminRoleRepository.findById(idLongReq.getId());
            if (!adminRoleOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的角色记录", idLongReq.getId()));
            } else {
                Role adminRole = adminRoleOptional.get();
                AdminRoleBo adminRoleBo = new AdminRoleBo();
                BeanUtils.copyProperties(adminRole, adminRoleBo);

                //处理菜单与按钮
                Set<AdminMenuButtonRef> adminMenuButtonRefs = adminRole.getAdminMenuButtons();
                if (null != adminMenuButtonRefs && !adminMenuButtonRefs.isEmpty()) {
                    List<Long> menuIds = adminMenuButtonRefs.stream().map(AdminMenuButtonRef::getMenuId)
                            .collect(Collectors.toList());
                    Iterable<AdminMenuButton> adminMenuButtons = adminMenuButtonRepository.findAllById(menuIds);
                    List<AdminMenuButtonBo> adminMenuButtonBos = Lists.newArrayList(adminMenuButtons).stream().map(adminMenuButton -> {
                        AdminMenuButtonBo adminMenuButtonBo = new AdminMenuButtonBo();
                        BeanUtils.copyProperties(adminMenuButton, adminMenuButtonBo);
                        return adminMenuButtonBo;
                    }).collect(Collectors.toList());
                    adminRoleBo.setMenus(adminMenuButtonBos);
                }
                return adminRoleBo;
            }
        }).map(adminRoleBo -> {
            AdminRoleBoRes adminRoleBoRes = new AdminRoleBoRes();
            adminRoleBoRes.setErrorCode(ApiErrorCode.ok.value);
            adminRoleBoRes.setReturnObject(adminRoleBo);
            return adminRoleBoRes;
        });
    }

    @Override
    @PostMapping("/softDelete")
    public Mono<BooleanRes> softDelete(@RequestBody Mono<IdsLongReq> idsLongReqMono) {
        return idsLongReqMono.map(idsLongReq -> {
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
    public Mono<AdminMenuButtonBoListRes> findAllMenuTree() {
        return Mono.fromCallable(() -> {
            Optional<List<AdminMenuButton>> optionalAdminMenuButtons = adminMenuButtonRepository.findAllEnable();
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
    @PostMapping("/findAllByAdminRoleId")
    public Mono<AdminMenuButtonBoListRes> findAllByAdminRoleId(@RequestBody Mono<IdLongReq> roleIdMono) {
        //参数验证
        return roleIdMono.map(idLongReq -> {
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