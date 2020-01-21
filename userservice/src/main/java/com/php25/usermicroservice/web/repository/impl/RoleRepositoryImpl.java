package com.php25.usermicroservice.web.repository.impl;

import com.php25.common.db.repository.BaseDbRepositoryImpl;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/7/26 09:43
 * @description:
 */
@Repository
public class RoleRepositoryImpl extends BaseDbRepositoryImpl<Role, Long> implements RoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean softDelete(List<Long> ids, String appId) {
        return jdbcTemplate.update("update t_role set enable=2 where id in (?)", ids) > 0;
    }

    @Override
    public Optional<Role> findByNameAndAppId(String name, String appId) {
        Role role = db.cndJdbc(Role.class).whereEq("name", name).andEq("appId", appId).andEq("enable", 1).single();
        if (null != role) {
            return Optional.of(role);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Boolean changeInfo(String description, Long id, String appId) {
        Role role = new Role();
        role.setId(id);
        role.setAppId(appId);
        role.setAppId(description);
        return db.cndJdbc(Role.class).update(role) > 0;
    }
}
