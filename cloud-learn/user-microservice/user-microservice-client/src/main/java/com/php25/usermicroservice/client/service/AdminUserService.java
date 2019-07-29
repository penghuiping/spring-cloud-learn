package com.php25.usermicroservice.client.service;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.dto.AdminUserDto;
import com.php25.usermicroservice.client.dto.ChangePasswordDto;
import com.php25.usermicroservice.client.dto.LoginDto;
import com.php25.usermicroservice.client.dto.SearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoListRes;
import com.php25.usermicroservice.client.dto.res.AdminUserDtoRes;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 此方法用于后台用户操作
 *
 * @author: penghuiping
 * @date: 2018/12/28 14:13
 * @description:
 */
public interface AdminUserService {

    /**
     * 后台用户登入
     *
     * @param loginDto 后台用户登入
     * @return 后台管理用户信息
     */
    Mono<AdminUserDtoRes> login(@Valid LoginDto loginDto);

    /**
     * 重置用户密码
     *
     * @param idsLongReq 需要重置用户的id
     * @return true:重置成功，false:重置失败
     */
    Mono<BooleanRes> resetPassword(@Valid IdsLongReq idsLongReq);

    /**
     * 修改某个后台用户密码
     *
     * @param changePasswordDto
     * @return true:修改密码成功,false:修改密码失败
     */
    Mono<BooleanRes> changePassword(@Valid ChangePasswordDto changePasswordDto);

    /**
     * 根据用户id获取详情
     *
     * @param idLongReq 后台管理用户id
     * @return 后台管理用户详情信息
     */
    Mono<AdminUserDtoRes> findOne(@Valid IdLongReq idLongReq);

    /**
     * 新增或者更新后台管理用户
     *
     * @param adminUserDto 后台管理用户
     * @return 新增或者更新后的用户信息
     */
    Mono<AdminUserDtoRes> save(@Valid AdminUserDto adminUserDto);


    /**
     * 软删除用户id
     *
     * @param idsLongReq 需要删除的后台管理用户id
     * @return Boolean true:软删除成功,false:软删除失败
     */
    Mono<BooleanRes> softDelete(@Valid IdsLongReq idsLongReq);

    /**
     * 分页查询
     *
     * @return 返回后台管理用户分页列表
     */
    Mono<AdminUserDtoListRes> query(@Valid SearchDto searchDto);

}
