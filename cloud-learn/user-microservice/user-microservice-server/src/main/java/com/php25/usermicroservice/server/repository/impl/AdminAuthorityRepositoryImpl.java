package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.AdminAuthority;
import com.php25.userservice.server.repository.AdminAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/10/17 14:20
 * @description:
 */
@Repository
public class AdminAuthorityRepositoryImpl extends BaseRepositoryImpl<AdminAuthority, Long> implements AdminAuthorityRepository {

    @Autowired
    private Db db;

    @Override
    public Optional<List<AdminAuthority>> findAllByAdminMenuButtonIds(List<Long> ids) {
        List<AdminAuthority> adminAuthorities = db.cnd(AdminAuthority.class).whereIn("admin_menu_button_id", ids).select();
        if (null != adminAuthorities && adminAuthorities.size() > 0) {
            return Optional.of(adminAuthorities);
        } else {
            return Optional.empty();
        }
    }
}
