package com.php25.usermicroservice.web.repository.impl;

import com.php25.common.db.repository.BaseDbRepositoryImpl;
import com.php25.usermicroservice.web.model.Group;
import com.php25.usermicroservice.web.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/25 16:35
 * @description:
 */
@Repository
public class GroupRepositoryImpl extends BaseDbRepositoryImpl<Group, Long> implements GroupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean softDelete(List<Long> ids, String appId) {
        return jdbcTemplate.update("update t_group set enable=2 where id in (?)", ids) > 0;
    }

    @Override
    public Boolean changeInfo(String description, Long id, String appId) {
        Group group = new Group();
        group.setId(id);
        group.setAppId(appId);
        group.setDescription(description);
        return db.cndJdbc(Group.class).update(group)>0;
    }
}
