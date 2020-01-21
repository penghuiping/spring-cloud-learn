package com.php25.usermicroservice.web.service;

import com.php25.common.db.specification.SearchParam;
import com.php25.usermicroservice.web.dto.RoleCreateDto;
import com.php25.usermicroservice.web.dto.RoleDetailDto;
import com.php25.usermicroservice.web.dto.RolePageDto;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 后台角色操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 10:58
 * @description:
 */
public interface RoleService {

    /**
     * 角色分页查询
     *
     * @param pageNum      当前第几页
     * @param pageSize     每页数量
     * @param searchParams 搜索参数
     * @param property     排序字段
     * @param direction    升序或者降序
     * @return 返回角色分页列表
     */
    List<RolePageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction);

    /**
     * 根据appId,使某个角色失效
     *
     * @param appId    应用客户端id
     * @param username 创建者用户名
     * @param roleId   角色id
     * @return true:成功,false:失败
     */
    Boolean unableRole(String appId, String username, Long roleId);


    /**
     * 根据appId,创建角色
     *
     * @param appId         应用客户端id
     * @param username      创建者用户名
     * @param roleCreateDto 角色信息
     * @return true:成功,false:失败
     */
    Boolean create(String appId, String username, RoleCreateDto roleCreateDto);


    /**
     * 根据appId,修改角色描述
     *
     * @param appId           应用id
     * @param username        创建者用户名
     * @param roleId          角色id
     * @param roleDescription 角色描述
     * @return true:成功,false:失败
     */
    Boolean changeInfo(String appId, String username, Long roleId, String roleDescription);


    /**
     * 查询角色详情
     *
     * @param roleId
     * @return
     */
    RoleDetailDto detailInfo(Long roleId);
}
