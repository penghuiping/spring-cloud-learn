package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.repository.JdbcDbRepositoryImpl;
import com.php25.usermicroservice.server.model.AdminUser;
import com.php25.usermicroservice.server.repository.AdminUserExRepository;

/**
 * @author: penghuiping
 * @date: 2019/7/25 16:35
 * @description:
 */
public class AdminUserExRepositoryImpl extends JdbcDbRepositoryImpl<AdminUser, Long> implements AdminUserExRepository {

}
