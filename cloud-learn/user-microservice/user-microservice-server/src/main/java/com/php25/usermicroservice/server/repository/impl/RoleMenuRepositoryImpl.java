package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.Db;
import com.php25.common.db.repository.BaseJpaRepositoryImpl;
import com.php25.usermicroservice.server.model.RoleMenu;
import com.php25.usermicroservice.server.repository.RoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2018/9/4 21:27
 * @description:
 */
@Repository
public class RoleMenuRepositoryImpl extends BaseJpaRepositoryImpl<RoleMenu, Long> implements RoleMenuRepository {

    @Autowired
    private Db db;

    @Override
    public RoleMenu findOneByRoleIdAndMenuId(Long roleId, Long menuId) {
        return db.cndJpa(RoleMenu.class).whereEq("adminRole", roleId).andEq("adminMenuButton", menuId).single();
    }
}
