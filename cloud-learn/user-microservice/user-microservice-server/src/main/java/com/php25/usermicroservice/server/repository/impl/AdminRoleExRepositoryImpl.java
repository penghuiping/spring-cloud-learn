package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.repository.JdbcDbRepositoryImpl;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.repository.AdminRoleExRepository;

/**
 * @author: penghuiping
 * @date: 2019/7/26 09:43
 * @description:
 */
public class AdminRoleExRepositoryImpl extends JdbcDbRepositoryImpl<AdminRole, Long> implements AdminRoleExRepository {
}
