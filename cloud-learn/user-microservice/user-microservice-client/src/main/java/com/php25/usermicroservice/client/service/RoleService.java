package com.php25.usermicroservice.client.service;

import com.php25.common.flux.web.ReqIdLong;
import com.php25.common.flux.web.ReqIdsLong;
import com.php25.usermicroservice.client.dto.req.ReqSearchDto;
import com.php25.usermicroservice.client.dto.res.ResAdminMenuButtonDtoList;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.dto.res.ResRoleDto;
import com.php25.usermicroservice.client.dto.res.ResRoleDtoList;
import com.php25.usermicroservice.client.dto.res.RoleDto;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 后台角色操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 10:58
 * @description:
 */
public interface RoleService {

    /**
     * 后台角色分页查询
     *
     * @param searchDto 搜索参数创建器
     * @return
     */
    Mono<ResRoleDtoList> query(@Valid ReqSearchDto searchDto);

    /**
     * 新增或者更新后台管理角色
     *
     * @param adminRoleDto 后台管理角色
     * @return 新增或者更新后的管理角色
     */
    Mono<ResRoleDto> save(@Valid RoleDto adminRoleDto);

    /**
     * 软删除后台管理角色id
     *
     * @param idsLongReq 需要删除的后台管理角色id
     * @return true:软删除成,false:软删除失败
     */
    Mono<ResBoolean> softDelete(@Valid ReqIdsLong idsLongReq);


    /**
     * 查询角色详情
     *
     * @param idLongReq
     * @return
     */
    Mono<ResRoleDto> findOne(@Valid ReqIdLong idLongReq);

    /**
     * 获取本系统中所有的菜单
     *
     * @return 返回本系统中所有的菜单按钮
     */
    Mono<ResAdminMenuButtonDtoList> findAllMenuTree();

    /**
     * 根据角色id获取这个角色对应的所有菜单按钮
     *
     * @param idLongReq 角色id
     * @return AdminMenuButtonRes 菜单与按钮
     */
    Mono<ResAdminMenuButtonDtoList> findAllByAdminRoleId(@Valid ReqIdLong idLongReq);

}
