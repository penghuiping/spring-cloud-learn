package com.php25.usermicroservice.server.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminAuthorityBo;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.rpc.AdminRoleRpc;
import com.php25.usermicroservice.server.model.AdminAuthority;
import com.php25.usermicroservice.server.model.AdminAuthorityRef;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminMenuButtonRef;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.repository.AdminAuthorityRepository;
import com.php25.usermicroservice.server.repository.AdminMenuButtonRepository;
import com.php25.usermicroservice.server.repository.AdminRoleRepository;
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

import javax.validation.Valid;
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
public class AdminRoleController implements AdminRoleRpc {

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private AdminMenuButtonRepository adminMenuButtonRepository;

    @Autowired
    private AdminAuthorityRepository adminAuthorityRepository;

    @Override
    @PostMapping("/query")
    public Mono<AdminRoleBoListRes> query(@RequestBody Mono<SearchBo> searchBoMono) {
        return searchBoMono.map(searchBo -> {
            var params = searchBo.getSearchParams().stream()
                    .map(searchParam -> SearchParam.of(searchParam.getFieldName(), searchParam.getOperator(), searchParam.getValue()))
                    .collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(params);
            Pageable pageable = PageRequest.of(searchBo.getPageNum(), searchBo.getPageSize(), searchBo.getDirection(), searchBo.getProperty());
            Page<AdminRole> adminRolePage = adminRoleRepository.findAll(searchParamBuilder, pageable);
            if (null != adminRolePage && adminRolePage.getTotalElements() > 0) {
                List<AdminRole> adminRoleList = adminRolePage.getContent();
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
            AdminRole adminRole = new AdminRole();
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

            //判断是否有authorities
            List<AdminAuthorityBo> adminAuthorityBos = adminRoleBo.getAuthorities();
            if (null != adminAuthorityBos && !adminAuthorityBos.isEmpty()) {
                //有的话就保存
                Set<AdminAuthorityRef> adminAuthorityRefs = adminAuthorityBos.stream().map(adminAuthorityBo -> {
                    AdminAuthorityRef adminAuthorityRef = new AdminAuthorityRef();
                    adminAuthorityRef.setAuthorityId(adminAuthorityBo.getId());
                    return adminAuthorityRef;
                }).collect(Collectors.toSet());
                adminRole.setAdminAuthorities(adminAuthorityRefs);
            }

            AdminRole adminRole1 = adminRoleRepository.save(adminRole);
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
            Optional<AdminRole> adminRoleOptional = adminRoleRepository.findById(idLongReq.getId());
            if (!adminRoleOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的角色记录", idLongReq.getId()));
            } else {
                AdminRole adminRole = adminRoleOptional.get();
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
                        adminMenuButtonBo.setParentId(adminMenuButton.getParent());
                        return adminMenuButtonBo;
                    }).collect(Collectors.toList());
                    adminRoleBo.setMenus(adminMenuButtonBos);
                }

                //处理权限
                Set<AdminAuthorityRef> adminAuthorityRefs = adminRole.getAdminAuthorities();
                if (null != adminAuthorityRefs && !adminAuthorityRefs.isEmpty()) {
                    List<Long> authorityIds = adminAuthorityRefs.stream().map(AdminAuthorityRef::getAuthorityId)
                            .collect(Collectors.toList());

                    Iterable<AdminAuthority> adminAuthorities = adminAuthorityRepository.findAllById(authorityIds);
                    List<AdminAuthorityBo> adminAuthorityBos = Lists.newArrayList(adminAuthorities).stream().map(adminAuthority -> {
                        AdminAuthorityBo adminAuthorityBo = new AdminAuthorityBo();
                        BeanUtils.copyProperties(adminAuthority, adminAuthorityBo);
                        return adminAuthorityBo;
                    }).collect(Collectors.toList());
                    adminRoleBo.setAuthorities(adminAuthorityBos);
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
            Iterable<AdminRole> adminRoles = adminRoleRepository.findAllById(idsLongReq.getIds());
            List<AdminRole> adminRoleList = Lists.newArrayList(adminRoles);
            if (null != adminRoleList && adminRoleList.size() > 0) {
                List<Long> ids = adminRoleList.stream().map(AdminRole::getId).collect(Collectors.toList());
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
}
