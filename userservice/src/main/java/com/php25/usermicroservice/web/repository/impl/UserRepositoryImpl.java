package com.php25.usermicroservice.web.repository.impl;

import com.php25.common.db.repository.BaseDbRepositoryImpl;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/7/25 16:35
 * @description:
 */
@Repository
public class UserRepositoryImpl extends BaseDbRepositoryImpl<User, Long> implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        User user = db.cndJdbc(User.class).whereEq("username", username).andEq("password", password).single();
        if (null != user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean updatePassword(String password, List<Long> ids) {
        return jdbcTemplate.update("update t_user set password=? where id in (?)", password, ids) > 0;
    }

    @Override
    public Boolean softDelete(List<Long> ids) {
        return jdbcTemplate.update("update t_user set enable=2 where id in (?)", ids) > 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user = db.cndJdbc(User.class).whereEq("username", username).andEq("enable", 1).single();
        if (null != user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByMobile(String mobile) {
        User user = db.cndJdbc(User.class).whereEq("mobile", mobile).andEq("enable", 1).single();
        if (null != user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = db.cndJdbc(User.class).whereEq("email", email).andEq("enable", 1).single();
        if (null != user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean changUserBasicInfo(Long userId, String nickname, String mobile, String email, String headImageId) {
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setHeadImageId(headImageId);
        return db.cndJdbc(User.class).update(user) > 0;
    }
}
