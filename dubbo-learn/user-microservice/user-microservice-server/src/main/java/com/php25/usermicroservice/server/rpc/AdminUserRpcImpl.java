package com.php25.usermicroservice.server.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.AssertUtil;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import com.php25.userservice.server.dto.AdminUserDto;
import com.php25.userservice.server.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/2 15:17
 * @description:
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service
public class AdminUserRpcImpl implements AdminUserRpc {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public ResultDto<AdminUserBo> login(String username, String password) {
        //参数效验
        AssertUtil.hasText(username, "username参数不能为空");
        AssertUtil.hasText(password, "password参数不能为空");
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword(username, password);
        if (!adminUserDtoOptional.isPresent()) {
            return new ResultDto<>(false, null);
        } else {
            AdminUserBo adminUserBo = new AdminUserBo();
            BeanUtils.copyProperties(adminUserDtoOptional.get(), adminUserBo);
            return new ResultDto<>(true, adminUserBo);
        }
    }

    @Override
    public Boolean resetPassword(List<Long> adminUserIds) {
        //参数效验
        AssertUtil.notEmpty(adminUserIds, "adminUserIds至少需要一个元素");
        //初始化密码为123456
        return adminUserService.updatePassword("123456", adminUserIds);
    }

    @Override
    public Boolean changePassword(Long adminUserId, String originPassword, String newPassword) {
        //参数效验
        AssertUtil.notNull(adminUserId, "adminUserId参数不能为null");
        AssertUtil.hasText(originPassword, "originPassword不能为空");
        AssertUtil.hasText(newPassword, "newPassword不能为空");

        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(adminUserId);
        if (!adminUserDtoOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", adminUserId));
        }

        AdminUserDto adminUserDto = adminUserDtoOptional.get();
        if (!adminUserDto.getPassword().equals(originPassword)) {
            throw new IllegalArgumentException(String.format("originPassword:%s与数据库的密码不一样", originPassword));
        }

        adminUserDto.setPassword(newPassword);
        Optional<AdminUserDto> adminUserDtoOptional1 = adminUserService.save(adminUserDto);
        if (adminUserDtoOptional1.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResultDto<AdminUserBo> findOne(Long id) {
        AssertUtil.notNull(id, "id数不能为null");
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(id);
        if (adminUserDtoOptional.isPresent()) {
            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            AdminUserBo adminUserBo = new AdminUserBo();
            BeanUtils.copyProperties(adminUserDto, adminUserBo);
            return new ResultDto<>(true, adminUserBo);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<AdminUserBo> save(AdminUserBo adminUserBo) {
        //参数验证
        AssertUtil.notNull(adminUserBo, "adminUserBo不能为null");
        AdminUserDto adminUserDto = new AdminUserDto();
        BeanUtils.copyProperties(adminUserBo, adminUserDto);
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.save(adminUserDto);
        if (adminUserDtoOptional.isPresent()) {
            adminUserBo.setId(adminUserDtoOptional.get().getId());
            return new ResultDto<>(true, adminUserBo);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public Boolean softDelete(List<Long> ids) {
        //参数效验
        AssertUtil.notEmpty(ids, "ids至少需要一个元素");
        Optional<List<AdminUserDto>> optionalAdminUserDtos = adminUserService.findAll(ids);
        if (optionalAdminUserDtos.isPresent() && !optionalAdminUserDtos.get().isEmpty()) {
            adminUserService.softDelete(optionalAdminUserDtos.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResultDto<DataGridPageDto<AdminUserBo>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort) {
        Optional<DataGridPageDto<AdminUserDto>> optionalAdminUserDtoDataGridPageDto =
                adminUserService.query(pageNum, pageSize, searchParamBuilder, BeanUtils::copyProperties, sort);
        if (optionalAdminUserDtoDataGridPageDto.isPresent()) {
            DataGridPageDto<AdminUserDto> dtoDataGridPageDto = optionalAdminUserDtoDataGridPageDto.get();
            List<AdminUserBo> adminUserBos = dtoDataGridPageDto.getData().stream().map(adminUserDto -> {
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDto, adminUserBo);
                return adminUserBo;
            }).collect(Collectors.toList());
            DataGridPageDto<AdminUserBo> adminUserBoDataGridPageDto = new DataGridPageDto<>();
            adminUserBoDataGridPageDto.setData(adminUserBos);
            adminUserBoDataGridPageDto.setDraw(dtoDataGridPageDto.getDraw());
            adminUserBoDataGridPageDto.setError(dtoDataGridPageDto.getError());
            adminUserBoDataGridPageDto.setRecordsFiltered(dtoDataGridPageDto.getRecordsFiltered());
            adminUserBoDataGridPageDto.setRecordsTotal(dtoDataGridPageDto.getRecordsTotal());
            adminUserBoDataGridPageDto.setsEcho(dtoDataGridPageDto.getsEcho());
            return new ResultDto<>(true, adminUserBoDataGridPageDto);
        } else {
            return new ResultDto<>(false, null);
        }
    }
}
