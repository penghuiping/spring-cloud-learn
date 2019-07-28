package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.repository.JdbcDbRepositoryImpl;
import com.php25.usermicroservice.server.model.User;
import com.php25.usermicroservice.server.repository.UserExRepository;

/**
 * @author: penghuiping
 * @date: 2019/7/25 16:35
 * @description:
 */
public class UserExRepositoryImpl extends JdbcDbRepositoryImpl<User, Long> implements UserExRepository {

}
