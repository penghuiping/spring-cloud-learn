package com.php25.usermicroservice.server.repository;

import com.php25.common.jdbc.repository.BaseRepository;
import com.php25.usermicroservice.server.model.AdminUser;

import java.util.List;

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

    /**
     * 批量更新密码
     *
     * @param password 密码
     * @param ids      后台用户ids
     * @return true:更新成功，false:更新失败
     */
    Boolean updatePassword(String password, List<Long> ids);

}
