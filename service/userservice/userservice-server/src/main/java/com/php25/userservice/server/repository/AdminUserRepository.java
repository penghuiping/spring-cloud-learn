package com.php25.userservice.server.repository;

import com.php25.common.jdbc.repository.BaseRepository;
import com.php25.userservice.server.model.AdminUser;

/**
 * @author penghuiping
 * @date 2015-01-19
 */
public interface AdminUserRepository extends BaseRepository<AdminUser, Long> {

    /**
     * 根据用户名与密码查询
     *
     * @param username
     * @param password
     * @return
     */
    AdminUser findByUsernameAndPassword(String username, String password);

}
