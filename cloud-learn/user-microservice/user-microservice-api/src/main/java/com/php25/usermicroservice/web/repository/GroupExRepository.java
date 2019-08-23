package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.JdbcDbRepository;
import com.php25.usermicroservice.web.model.Group;

/**
 * @author: penghuiping
 * @date: 2019/7/26 09:42
 * @description:
 */
public interface GroupExRepository extends JdbcDbRepository<Group, Long> {
}
