package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.IdLongReq;
import com.php25.common.flux.IdsLongReq;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;
import com.php25.usermicroservice.client.bo.HasRightAccessUrlBo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 菜单与按钮操作类
 *
 * @author: penghuiping
 * @date: 2019/1/2 11:06
 * @description:
 */
public interface AdminMenuRpc {

    /**
     * 获取本系统中所有的菜单
     *
     * @return 返回本系统中所有的菜单按钮
     */
    Flux<AdminMenuButtonBo> findAllMenuTree();


    /**
     * 判断这个后台管理用户是否有权限访问次url
     *
     * @param hasRightAccessUrlBoMono
     * @return true:有权限访问,false:无权限访问
     */
    Mono<Boolean> hasRightAccessUrl(Mono<HasRightAccessUrlBo> hasRightAccessUrlBoMono);


    /**
     * 根据角色id获取这个角色对应的所有菜单按钮
     *
     * @param idLongReqMono 角色id
     * @return AdminMenuButtonRes 菜单与按钮
     */
    Flux<AdminMenuButtonBo> findAllByAdminRoleId(Mono<IdLongReq> idLongReqMono);

}
