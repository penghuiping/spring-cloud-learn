package com.php25.usermicroservice.server.controller;

import com.php25.common.core.dto.DataGridPageDto;
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
import com.php25.usermicroservice.server.dto.AdminRoleDto;
import com.php25.usermicroservice.server.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminRole")
public class AdminRoleController implements AdminRoleRpc {

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    @PostMapping("/query")
    public Mono<AdminRoleBoListRes> query(@RequestBody Mono<SearchBo> searchBoMono) {
        return searchBoMono.map(searchBo -> {
            var params = searchBo.getSearchParams().stream()
                    .map(searchParam -> SearchParam.of(searchParam.getFieldName(), searchParam.getOperator(), searchParam.getValue())).collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(params);
            Optional<DataGridPageDto<AdminRoleDto>> optionalAdminRoleDtoDataGridPageDto = adminRoleService.query(searchBo.getPageNum(), searchBo.getPageSize(), searchParamBuilder, BeanUtils::copyProperties, Sort.by(searchBo.getDirection(), searchBo.getProperty()));
            if (optionalAdminRoleDtoDataGridPageDto.isPresent()) {
                DataGridPageDto<AdminRoleDto> dataGridPageDto = optionalAdminRoleDtoDataGridPageDto.get();
                if (null != dataGridPageDto.getData()) {
                    return optionalAdminRoleDtoDataGridPageDto.get().getData().stream().map(adminRoleDto -> {
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
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRoleBo, adminRoleDto);
            Optional<AdminRoleDto> adminRoleDtoOptional = adminRoleService.save(adminRoleDto);
            if (adminRoleDtoOptional.isPresent()) {
                adminRoleBo.setId(adminRoleDtoOptional.get().getId());
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
            Optional<List<AdminRoleDto>> adminRoleDtoOptional = adminRoleService.findAll(idsLongReq.getIds(), true);
            if (adminRoleDtoOptional.isPresent() && !adminRoleDtoOptional.get().isEmpty()) {
                adminRoleService.softDelete(adminRoleDtoOptional.get());
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
