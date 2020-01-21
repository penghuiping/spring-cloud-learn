package com.php25.usermicroservice.web.service;

import com.php25.common.db.specification.SearchParam;
import com.php25.usermicroservice.web.dto.GroupCreateDto;
import com.php25.usermicroservice.web.dto.GroupDetailDto;
import com.php25.usermicroservice.web.dto.GroupPageDto;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分组操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 10:58
 * @description:
 */
public interface GroupService {

    /**
     * 组分页查询
     *
     * @param pageNum      当前第几页
     * @param pageSize     每页数量
     * @param searchParams 搜索参数
     * @param property     排序字段
     * @param direction    升序或者降序
     * @return 返回组分页列表
     */
    List<GroupPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction);

    /**
     * 根据appId,使某个组失效
     *
     * @param appId   应用客户端id
     * @param groupId 角色id
     * @return true:成功,false:失败
     */
    Boolean unableGroup(String appId,String username, Long groupId);


    /**
     * 根据appId,创建组
     *
     * @param appId          应用客户端id
     * @param groupCreateDto 角色信息
     * @return true:成功,false:失败
     */
    Boolean create(String appId, String username, GroupCreateDto groupCreateDto);


    /**
     * 根据appId,修改组描述
     *
     * @param appId            应用id
     * @param groupId          组id
     * @param groupDescription 组描述
     * @return true:成功,false:失败
     */
    Boolean changeInfo(String appId, String username,Long groupId, String groupDescription);


    /**
     * 查询组详情
     *
     * @param groupId
     * @return
     */
    GroupDetailDto detailInfo(Long groupId);
}
