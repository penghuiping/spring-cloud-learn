package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.repository.JdbcDbRepositoryImpl;
import com.php25.usermicroservice.server.model.Role;
import com.php25.usermicroservice.server.repository.RoleExRepository;

/**
 * @author: penghuiping
 * @date: 2019/7/26 09:43
 * @description:
 */
public class RoleExRepositoryImpl extends JdbcDbRepositoryImpl<Role, Long> implements RoleExRepository {
}
