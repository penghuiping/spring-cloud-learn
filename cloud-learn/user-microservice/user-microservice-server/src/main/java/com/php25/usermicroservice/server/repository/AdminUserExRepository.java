package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.JdbcDbRepository;
import com.php25.usermicroservice.server.model.AdminUser;

/**
 * @author: penghuiping
 * @date: 2019/7/25 16:40
 * @description:
 */
public interface AdminUserExRepository extends JdbcDbRepository<AdminUser,Long> {
}
