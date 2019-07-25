package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.Db;
import com.php25.common.db.repository.BaseJpaRepositoryImpl;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.model.RoleMenu;
import com.php25.usermicroservice.server.repository.AdminMenuButtonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2018/8/29 16:26
 * @description:
 */
@Repository
public class AdminMenuButtonRepositoryImpl extends BaseJpaRepositoryImpl<AdminMenuButton, Long> implements AdminMenuButtonRepository {

    @Autowired
    private Db db;

    @Override
    public List<AdminMenuButton> findRootMenus() {
        return db.cndJpa(AdminMenuButton.class).whereIsNull("parent").andNotEq("enable", 2).asc("sort").select();
    }

    @Override
    public List<AdminMenuButton> findMenusByParent(AdminMenuButton parent) {
        return db.cndJpa(AdminMenuButton.class).whereEq("parent", parent.getId()).andNotEq("enable", 2).select();
    }

    @Override
    public List<AdminMenuButton> findMenusByRole(AdminRole role) {
        return db.cndJpa(RoleMenu.class).join(AdminMenuButton.class, "adminMenuButton")
                .whereEq("adminRole", role.getId())
                .select(AdminMenuButton.class);
    }

    @Override
    public List<AdminMenuButton> findRootMenusEnabled() {
        return db.cndJpa(AdminMenuButton.class).whereIsNull("parent").andEq("enable", 1).select();
    }

    @Override
    public List<AdminMenuButton> findMenusEnabledByParent(AdminMenuButton parent) {
        return db.cndJpa(AdminMenuButton.class)
                .whereEq("parent", parent.getId()).andEq("enable", 1)
                .asc("sort").select();
    }

    @Override
    public List<AdminMenuButton> findMenusEnabledByRole(AdminRole role) {
        return db.cndJpa(RoleMenu.class)
                .join(AdminMenuButton.class, "adminMenuButton")
                .whereEq("adminRole", role.getId()).select(AdminMenuButton.class);
    }

    @Override
    public List<AdminMenuButton> findMenusEnabledByParentAndRole(AdminMenuButton parent, AdminRole role) {
        return db.cndJpa(RoleMenu.class)
                .join(AdminMenuButton.class, "adminMenuButton")
                .whereEq("adminRole", role.getId())
                .andEq(AdminMenuButton.class, "parent", parent.getId())
                .select(AdminMenuButton.class);
    }

    @Override
    public Integer findMenusMaxSort() {
        List<Map> list = db.cndJpa(AdminMenuButton.class).whereEq("enable", 1).mapSelect("max(sort) as maxSort");
        if (null != list && list.size() > 0) {
            return Integer.parseInt(list.get(0).get("maxSort").toString());
        } else {
            return -1;
        }
    }
}
