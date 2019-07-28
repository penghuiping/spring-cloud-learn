package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.ChangePasswordBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminUserBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminUserBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
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
     * @param loginBoMono 后台用户登入
     * @return 后台管理用户信息
     */
    Mono<AdminUserBoRes> login(@Valid Mono<LoginBo> loginBoMono);

    /**
     * 重置用户密码
     *
     * @param idsLongReqMono 需要重置用户的id
     * @return true:重置成功，false:重置失败
     */
    Mono<BooleanRes> resetPassword(@Valid Mono<IdsLongReq> idsLongReqMono);

    /**
     * 修改某个后台用户密码
     *
     * @param changePasswordBoMono
     * @return true:修改密码成功,false:修改密码失败
     */
    Mono<BooleanRes> changePassword(@Valid Mono<ChangePasswordBo> changePasswordBoMono);

    /**
     * 根据用户id获取详情
     *
     * @param idLongReqMono 后台管理用户id
     * @return 后台管理用户详情信息
     */
    Mono<AdminUserBoRes> findOne(@Valid Mono<IdLongReq> idLongReqMono);

    /**
     * 新增或者更新后台管理用户
     *
     * @param adminUserBoMono 后台管理用户
     * @return 新增或者更新后的用户信息
     */
    Mono<AdminUserBoRes> save(@Valid Mono<AdminUserBo> adminUserBoMono);


    /**
     * 软删除用户id
     *
     * @param idsLongReqMono 需要删除的后台管理用户id
     * @return Boolean true:软删除成功,false:软删除失败
     */
    Mono<BooleanRes> softDelete(@Valid Mono<IdsLongReq> idsLongReqMono);

    /**
     * 分页查询
     *
     * @return 返回后台管理用户分页列表
     */
    Mono<AdminUserBoListRes> query(@Valid Mono<SearchBo> searchBoMono);

}
