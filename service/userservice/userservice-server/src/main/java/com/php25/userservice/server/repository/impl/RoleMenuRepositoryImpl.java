package com.php25.userservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.RoleMenu;
import com.php25.userservice.server.repository.RoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2018/9/4 21:27
 * @description:
 */
@Repository
public class RoleMenuRepositoryImpl extends BaseRepositoryImpl<RoleMenu, Long> implements RoleMenuRepository {

    @Autowired
    private Db db;

    @Override
    public RoleMenu findOneByRoleIdAndMenuId(Long roleId, Long menuId) {
        return db.cnd(RoleMenu.class).whereEq("adminRole", roleId).andEq("adminMenuButton", menuId).single();
    }
}
