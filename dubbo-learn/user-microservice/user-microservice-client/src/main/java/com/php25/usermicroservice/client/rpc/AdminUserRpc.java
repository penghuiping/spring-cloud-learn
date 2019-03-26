package com.php25.usermicroservice.client.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 此方法用于后台用户操作
 *
 * @author: penghuiping
 * @date: 2018/12/28 14:13
 * @description:
 */
public interface AdminUserRpc {

    /**
     * 后台用户登入
     *
     * @param username 后台用户名
     * @param password 后台密码
     * @return 后台管理用户信息
     */
    ResultDto<AdminUserBo> login(String username, String password);

    /**
     * 重置用户密码
     *
     * @param adminUserIds 需要重置用户的id
     * @return true:重置成功，false:重置失败
     */
    Boolean resetPassword(List<Long> adminUserIds);

    /**
     * 修改某个后台用户密码
     *
     * @param adminUserId    后台管理用户id
     * @param originPassword 原来的密码
     * @param newPassword    新密码
     * @return true:修改密码成功,false:修改密码失败
     */
    Boolean changePassword(Long adminUserId, String originPassword, String newPassword);

    /**
     * 根据用户id获取详情
     *
     * @param id 后台管理用户id
     * @return 后台管理用户详情信息
     */
    ResultDto<AdminUserBo> findOne(Long id);

    /**
     * 新增或者更新后台管理用户
     *
     * @param adminUserDto 后台管理用户
     * @return 新增或者更新后的用户信息
     */
    ResultDto<AdminUserBo> save(AdminUserBo adminUserDto);


    /**
     * 软删除用户id
     *
     * @param ids 需要删除的后台管理用户id
     * @return Boolean true:软删除成功,false:软删除失败
     */
    Boolean softDelete(List<Long> ids);

    /**
     * 分页查询
     *
     * @param searchParamBuilder 搜索参数创建器
     * @param pageNum            当前第几页
     * @param pageSize           一页多少条数据
     * @return 返回后台管理用户分页列表
     */
    ResultDto<DataGridPageDto<AdminUserBo>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort);

}
