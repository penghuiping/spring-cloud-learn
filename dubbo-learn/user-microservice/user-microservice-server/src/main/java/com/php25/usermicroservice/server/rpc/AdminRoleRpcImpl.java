package com.php25.usermicroservice.server.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.rpc.AdminRoleRpc;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResultDto<DataGridPageDto<AdminRoleBo>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort) {
        Optional<DataGridPageDto<AdminRoleDto>> optionalAdminRoleDtoDataGridPageDto = adminRoleService.query(pageNum, pageSize, searchParamBuilder, BeanUtils::copyProperties, sort);
        if (optionalAdminRoleDtoDataGridPageDto.isPresent()) {
            DataGridPageDto<AdminRoleDto> dataGridPageDto = optionalAdminRoleDtoDataGridPageDto.get();
            if (null != dataGridPageDto.getData()) {
                List<AdminRoleBo> adminRoleBos = optionalAdminRoleDtoDataGridPageDto.get().getData().stream().map(adminRoleDto -> {
                    AdminRoleBo adminRoleBo = new AdminRoleBo();
                    BeanUtils.copyProperties(adminRoleDto, adminRoleBo);
                    return adminRoleBo;
                }).collect(Collectors.toList());
                DataGridPageDto<AdminRoleBo> adminRoleBoDataGridPageDto = new DataGridPageDto<AdminRoleBo>();
                adminRoleBoDataGridPageDto.setData(adminRoleBos);
                adminRoleBoDataGridPageDto.setDraw(dataGridPageDto.getDraw());
                adminRoleBoDataGridPageDto.setError(dataGridPageDto.getError());
                adminRoleBoDataGridPageDto.setRecordsFiltered(dataGridPageDto.getRecordsFiltered());
                adminRoleBoDataGridPageDto.setRecordsTotal(dataGridPageDto.getRecordsTotal());
                adminRoleBoDataGridPageDto.setsEcho(dataGridPageDto.getsEcho());
                return new ResultDto<>(true, adminRoleBoDataGridPageDto);
            }
        }
        return new ResultDto<>(false, null);
    }

    @Override
    public ResultDto<AdminRoleBo> save(AdminRoleBo adminRolebo) {
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRolebo, adminRoleDto);
        Optional<AdminRoleDto> adminRoleDtoOptional = adminRoleService.save(adminRoleDto);
        if (adminRoleDtoOptional.isPresent()) {
            adminRolebo.setId(adminRoleDtoOptional.get().getId());
            return new ResultDto<>(true, adminRolebo);
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
