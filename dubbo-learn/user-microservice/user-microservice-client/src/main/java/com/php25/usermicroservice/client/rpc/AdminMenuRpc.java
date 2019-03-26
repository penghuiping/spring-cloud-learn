package com.php25.usermicroservice.client.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.usermicroservice.client.bo.AdminMenuButtonBo;

import java.util.List;

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
    ResultDto<List<AdminMenuButtonBo>> findAllMenuTree();


    /**
     * 判断这个后台管理用户是否有权限访问次url
     *
     * @param url         需要访问的url
     * @param adminUserId 访问人
     * @return true:有权限访问,false:无权限访问
     */
    Boolean hasRightAccessUrl(String url, Long adminUserId);


    /**
     * 根据角色id获取这个角色对应的所有菜单按钮
     *
     * @param roleId
     * @return 返回角色对应的菜单按钮
     */
    ResultDto<List<AdminMenuButtonBo>> findAllByAdminRoleId(Long roleId);

}
