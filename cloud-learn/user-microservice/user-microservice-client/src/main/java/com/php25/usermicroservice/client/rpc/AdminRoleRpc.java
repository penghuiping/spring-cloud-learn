package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.BooleanRes;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminRoleBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
     * @param searchBo 搜索参数创建器
     * @return
     */
    Flux<AdminRoleBo> query(SearchBo searchBo);

    /**
     * 新增或者更新后台管理角色
     *
     * @param adminRoleDto 后台管理角色
     * @return 新增或者更新后的管理角色
     */
    Mono<AdminRoleBo> save(AdminRoleBo adminRoleDto);

    /**
     * 软删除后台管理角色id
     *
     * @param idsLongReqMono 需要删除的后台管理角色id
     * @return true:软删除成,false:软删除失败
     */
    Mono<BooleanRes> softDelete(Mono<IdsLongReq> idsLongReqMono);


}
