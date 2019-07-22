package com.php25.usermicroservice.server.controller;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<AdminRoleBo> query(@RequestBody SearchBo searchBo) {
        return Mono.fromCallable(() -> {
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
        }).flatMapMany(Flux::fromIterable).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminRoleBo> save(@RequestBody AdminRoleBo adminRolebo) {
        log.info("....save");
        return Mono.fromCallable(() -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRolebo, adminRoleDto);
            Optional<AdminRoleDto> adminRoleDtoOptional = adminRoleService.save(adminRoleDto);
            if (adminRoleDtoOptional.isPresent()) {
                adminRolebo.setId(adminRoleDtoOptional.get().getId());
                return adminRolebo;
            } else {
                return null;
            }
        }).flatMap(adminRoleBo -> {
            if (null == adminRoleBo) {
                return Mono.empty();
            } else {
                return Mono.just(adminRoleBo);
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<Boolean> softDelete(@RequestBody List<Long> ids) {
        return Mono.fromCallable(() -> {
            Optional<List<AdminRoleDto>> adminRoleDtoOptional = adminRoleService.findAll(ids, true);
            if (adminRoleDtoOptional.isPresent() && !adminRoleDtoOptional.get().isEmpty()) {
                adminRoleService.softDelete(adminRoleDtoOptional.get());
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }
}
