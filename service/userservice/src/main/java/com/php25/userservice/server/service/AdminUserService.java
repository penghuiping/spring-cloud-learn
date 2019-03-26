package com.php25.userservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.userservice.server.dto.AdminUserDto;
import com.php25.userservice.server.model.AdminUser;

import java.util.List;
import java.util.Optional;

/**
 * 后台用户
 *
 * @author penghuiping
 * @date 2016-08-12
 */
public interface AdminUserService extends BaseService<AdminUserDto, AdminUser, Long>, SoftDeletable<AdminUserDto> {

    /**
     * 根据用户名与密码获取用户信息
     *
     * @param loginName 用户名
     * @param password  密码
     * @return AdminUserDto
     */
    Optional<AdminUserDto> findByUsernameAndPassword(String loginName, String password);


    /**
     * 根据id获取集合列表
     *
     * @param ids  后台管理用户id集合
     * @param lazy 对于对象里面的集合是否进行懒加载操作
     * @return List<AdminUserDto>
     */
    Optional<List<AdminUserDto>> findAll(Iterable<Long> ids, Boolean lazy);

    /**
     * 批量更新后台用户密码
     *
     * @param password 密码
     * @param ids      后台用户id
     * @return true:批量更新成功,false:批量更新失败
     */
    Boolean updatePassword(String password, List<Long> ids);


}
