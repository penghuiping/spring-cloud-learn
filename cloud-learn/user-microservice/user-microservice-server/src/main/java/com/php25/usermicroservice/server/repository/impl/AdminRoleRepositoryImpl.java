package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.db.repository.BaseJpaRepositoryImpl;
import com.php25.usermicroservice.server.model.AdminRole;
import com.php25.usermicroservice.server.repository.AdminRoleRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2018/8/29 11:09
 * @description:
 */
@Repository
public class AdminRoleRepositoryImpl extends BaseJpaRepositoryImpl<AdminRole, Long> implements AdminRoleRepository {


}
