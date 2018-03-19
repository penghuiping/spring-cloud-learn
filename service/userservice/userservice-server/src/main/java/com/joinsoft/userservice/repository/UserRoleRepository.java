package com.joinsoft.userservice.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.userservice.model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Zhangbing on 2017/4/24.
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole,String> {

    @Query("from UserRole u where u.adminRole.id=:roleId and u.adminUser.id=:userId")
    UserRole findOneByRoleIdAndUserId(@Param("roleId") String roleId,@Param("userId")String userId);
}
