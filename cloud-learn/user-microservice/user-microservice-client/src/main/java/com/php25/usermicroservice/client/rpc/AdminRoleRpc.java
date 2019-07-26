package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoListRes;
import com.php25.usermicroservice.client.bo.res.AdminRoleBoRes;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 后台角色操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 10:58
 * @description:
 */
public interface AdminRoleRpc {

    /**
     * 后台角色分页查询
     *
     * @param searchBoMono 搜索参数创建器
     * @return
     */
    Mono<AdminRoleBoListRes> query(@Valid Mono<SearchBo> searchBoMono);

    /**
     * 新增或者更新后台管理角色
     *
     * @param adminRoleBoMono 后台管理角色
     * @return 新增或者更新后的管理角色
     */
    Mono<AdminRoleBoRes> save(@Valid Mono<AdminRoleBo> adminRoleBoMono);

    /**
     * 软删除后台管理角色id
     *
     * @param idsLongReqMono 需要删除的后台管理角色id
     * @return true:软删除成,false:软删除失败
     */
    Mono<BooleanRes> softDelete(@Valid Mono<IdsLongReq> idsLongReqMono);


    /**
     * 查询角色详情
     *
     * @param idLongReqMono
     * @return
     */
    Mono<AdminRoleBoRes> findOne(@Valid Mono<IdLongReq> idLongReqMono);


}
