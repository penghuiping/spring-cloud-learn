package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.JdbcDbRepository;
import com.php25.usermicroservice.server.model.AdminRole;

/**
 * @author: penghuiping
 * @date: 2019/7/26 09:42
 * @description:
 */
public interface AdminRoleExRepository extends JdbcDbRepository<AdminRole, Long> {
}
