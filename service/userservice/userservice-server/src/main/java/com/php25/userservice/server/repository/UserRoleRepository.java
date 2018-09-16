package com.php25.userservice.server.repository;

import com.php25.common.jdbc.repository.BaseRepository;
import com.php25.userservice.server.model.UserRole;
import org.springframework.data.repository.query.Param;

/**
 * @author penghuiping
 * @date 2017/4/24.
 */
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    UserRole findOneByRoleIdAndUserId(@Param("roleId") Long roleId, @Param("userId") Long userId);
}
