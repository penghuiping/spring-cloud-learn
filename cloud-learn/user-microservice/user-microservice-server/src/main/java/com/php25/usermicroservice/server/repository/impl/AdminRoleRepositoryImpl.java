package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.AdminRole;
import com.php25.userservice.server.repository.AdminRoleRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2018/8/29 11:09
 * @description:
 */
@Repository
public class AdminRoleRepositoryImpl extends BaseRepositoryImpl<AdminRole, Long> implements AdminRoleRepository {


}
