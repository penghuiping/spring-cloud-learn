package com.php25.usermicroservice.client.service;

import com.php25.common.flux.web.ReqIdLong;
import com.php25.common.flux.web.ReqIdsLong;
import com.php25.usermicroservice.client.dto.req.ReqChangePasswordDto;
import com.php25.usermicroservice.client.dto.req.ReqLoginDto;
import com.php25.usermicroservice.client.dto.req.ReqSearchDto;
import com.php25.usermicroservice.client.dto.res.AdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDto;
import com.php25.usermicroservice.client.dto.res.ResAdminUserDtoList;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 此方法用于后台用户操作
 *
 * @author: penghuiping
 * @date: 2018/12/28 14:13
 * @description:
 */
public interface UserService {

    /**
     * 后台用户登入
     *
     * @param loginDto 后台用户登入
     * @return 后台管理用户信息
     */
    Mono<ResAdminUserDto> login(@Valid ReqLoginDto loginDto);

    /**
     * 重置用户密码
     *
     * @param idsLongReq 需要重置用户的id
     * @return true:重置成功，false:重置失败
     */
    Mono<ResBoolean> resetPassword(@Valid ReqIdsLong idsLongReq);

    /**
     * 修改某个后台用户密码
     *
     * @param changePasswordDto
     * @return true:修改密码成功,false:修改密码失败
     */
    Mono<ResBoolean> changePassword(@Valid ReqChangePasswordDto changePasswordDto);

    /**
     * 根据用户id获取详情
     *
     * @param idLongReq 后台管理用户id
     * @return 后台管理用户详情信息
     */
    Mono<ResAdminUserDto> findOne(@Valid ReqIdLong idLongReq);

    /**
     * 新增或者更新后台管理用户
     *
     * @param adminUserDto 后台管理用户
     * @return 新增或者更新后的用户信息
     */
    Mono<ResAdminUserDto> save(@Valid AdminUserDto adminUserDto);


    /**
     * 软删除用户id
     *
     * @param idsLongReq 需要删除的后台管理用户id
     * @return Boolean true:软删除成功,false:软删除失败
     */
    Mono<ResBoolean> softDelete(@Valid ReqIdsLong idsLongReq);

    /**
     * 分页查询
     *
     * @return 返回后台管理用户分页列表
     */
    Mono<ResAdminUserDtoList> query(@Valid ReqSearchDto searchDto);

}
