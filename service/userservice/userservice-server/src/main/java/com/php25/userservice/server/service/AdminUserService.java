package com.php25.userservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.userservice.client.dto.AdminUserDto;
import com.php25.userservice.server.model.AdminUser;

import java.util.List;
import java.util.Optional;

/**
 * 后台用户
 *
 * @author penghuiping
 * @Time 2016-08-12
 */
public interface AdminUserService extends BaseService<AdminUserDto, AdminUser, Long>, SoftDeletable<AdminUserDto> {

    /**
     * 根据用户名与密码获取用户信息
     *
     * @param loginName
     * @param password
     * @return AdminUserDto
     * @author penghuiping
     * @Time 2016-08-12
     */
    Optional<AdminUserDto> findByUsernameAndPassword(String loginName, String password);


    /**
     * 根据id获取集合列表
     *
     * @param ids
     * @param lazy 对于对象里面的集合是否进行懒加载操作
     * @return
     */
    Optional<List<AdminUserDto>> findAll(Iterable<Long> ids, Boolean lazy);
}
