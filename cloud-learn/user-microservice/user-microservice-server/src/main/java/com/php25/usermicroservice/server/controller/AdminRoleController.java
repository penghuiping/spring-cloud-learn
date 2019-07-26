package com.php25.usermicroservice.server.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.rpc.AdminRoleRpc;
import com.php25.usermicroservice.server.model.AdminRole;
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

import java.util.ArrayList;
import java.util.List;
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
