package com.php25.userservice.server.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.userservice.client.dto.AdminUserDto;
import com.php25.userservice.client.rpc.AdminUserRpc;
import com.php25.userservice.server.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

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
    public ResultDto<AdminUserDto> login(String username, String password) {
        //参数效验
        Assert.hasText(username, "username参数不能为空");
        Assert.hasText(password, "password参数不能为空");
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword(username, password);
        if (!adminUserDtoOptional.isPresent()) {
            return new ResultDto<>(false, null);
        } else {
            return new ResultDto<>(true, adminUserDtoOptional.get());
        }
    }

    @Override
    public Boolean resetPassword(List<Long> adminUserIds) {
        //参数效验
        Assert.notEmpty(adminUserIds, "adminUserIds至少需要一个元素");
        //初始化密码为123456
        return adminUserService.updatePassword("123456", adminUserIds);
    }

    @Override
    public Boolean changePassword(Long adminUserId, String originPassword, String newPassword) {
        //参数效验
        Assert.notNull(adminUserId, "adminUserId参数不能为null");
        Assert.hasText(originPassword, "originPassword不能为空");
        Assert.hasText(newPassword, "newPassword不能为空");

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
    public ResultDto<AdminUserDto> findOne(Long id) {
        Assert.notNull(id, "id数不能为null");
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(id);
        if (adminUserDtoOptional.isPresent()) {
            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            return new ResultDto<>(true, adminUserDto);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<AdminUserDto> save(AdminUserDto adminUserDto) {
        //参数验证
        Assert.notNull(adminUserDto, "adminUserDto不能为null");
        Optional<AdminUserDto> adminUserDtoOptional = adminUserService.save(adminUserDto);
        if (adminUserDtoOptional.isPresent()) {
            return new ResultDto<>(true, adminUserDtoOptional.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public Boolean softDelete(List<Long> ids) {
        //参数效验
        Assert.notEmpty(ids, "ids至少需要一个元素");
        Optional<List<AdminUserDto>> optionalAdminUserDtos = adminUserService.findAll(ids);
        if (optionalAdminUserDtos.isPresent() && !optionalAdminUserDtos.get().isEmpty()) {
            adminUserService.softDelete(optionalAdminUserDtos.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResultDto<DataGridPageDto<AdminUserDto>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort) {
        Optional<DataGridPageDto<AdminUserDto>> optionalAdminUserDtoDataGridPageDto =
                adminUserService.query(pageNum, pageSize, searchParamBuilder, BeanUtils::copyProperties, sort);
        if (optionalAdminUserDtoDataGridPageDto.isPresent()) {
            return new ResultDto<>(true, optionalAdminUserDtoDataGridPageDto.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }
}
