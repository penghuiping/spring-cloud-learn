package com.php25.userservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.AdminUser;
import com.php25.userservice.server.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author penghuiping
 * @date 2015-02-20
 */
@Repository
public class AdminUserRepositoryImpl extends BaseRepositoryImpl<AdminUser, Long> implements AdminUserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Db db;

    @Transactional
    @Override
    public AdminUser findByUsernameAndPassword(String loginName, String password) {
        return db.cnd(AdminUser.class).whereEq("username", loginName).andEq("password", password).andEq("enable", 1).single();
    }

    @Override
    public Boolean updatePassword(String password, List<Long> ids) {
        String idsStr = ids.toString().substring(1, ids.size() - 1);
        int rows = jdbcTemplate.update("update userservice_user a set a.password=? where a.id in ?", password, idsStr);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }
}
