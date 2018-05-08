package com.php25.userservice.server.repository;

import com.php25.common.repository.BaseRepository;
import com.php25.userservice.server.model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Zhangbing on 2017/4/24.
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    @Query("from UserRole u where u.adminRole.id=:roleId and u.adminUser.id=:userId")
    UserRole findOneByRoleIdAndUserId(@Param("roleId") Long roleId, @Param("userId") Long userId);
}
