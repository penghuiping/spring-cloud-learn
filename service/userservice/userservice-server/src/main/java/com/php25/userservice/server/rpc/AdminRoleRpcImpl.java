package com.php25.userservice.server.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.client.rpc.AdminRoleRpc;
import com.php25.userservice.server.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service
public class AdminRoleRpcImpl implements AdminRoleRpc {

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public ResultDto<DataGridPageDto<AdminRoleDto>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort) {
        Optional<DataGridPageDto<AdminRoleDto>> optionalAdminRoleDtoDataGridPageDto = adminRoleService.query(pageNum, pageSize, searchParamBuilder, BeanUtils::copyProperties, sort);
        if (optionalAdminRoleDtoDataGridPageDto.isPresent()) {
            return new ResultDto<>(true, optionalAdminRoleDtoDataGridPageDto.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<AdminRoleDto> save(AdminRoleDto adminRoleDto) {
        Optional<AdminRoleDto> adminRoleDtoOptional = adminRoleService.save(adminRoleDto);
        if (adminRoleDtoOptional.isPresent()) {
            return new ResultDto<>(true, adminRoleDtoOptional.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public Boolean softDelete(List<Long> ids) {
        Optional<List<AdminRoleDto>> adminRoleDtoOptional = adminRoleService.findAll(ids, true);
        if (adminRoleDtoOptional.isPresent() && !adminRoleDtoOptional.get().isEmpty()) {
            adminRoleService.softDelete(adminRoleDtoOptional.get());
            return true;
        } else {
            return false;
        }
    }
}
