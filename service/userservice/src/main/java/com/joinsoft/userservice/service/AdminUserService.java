package com.joinsoft.userservice.service;

import com.joinsoft.common.service.BaseService;
import com.joinsoft.common.service.SoftDeletable;
import com.joinsoft.userservice.dto.AdminUserDto;
import com.joinsoft.userservice.model.AdminUser;

import java.util.List;

/**
 * 后台用户
 *
 * @author penghuiping
 * @Time 2016-08-12
 */
public interface AdminUserService extends BaseService<AdminUserDto, AdminUser>, SoftDeletable<AdminUserDto> {

    /**
     * 根据用户名与密码获取用户信息
     *
     * @param loginName
     * @param password
     * @return AdminUserDto
     * @author penghuiping
     * @Time 2016-08-12
     */
    AdminUserDto findByLoginNameAndPassword(String loginName, String password);


    /**
     * 根据id获取集合列表
     *
     * @param ids
     * @param lazy 对于对象里面的集合是否进行懒加载操作
     * @return
     */
    List<AdminUserDto> findAll(Iterable<String> ids, Boolean lazy);
}
