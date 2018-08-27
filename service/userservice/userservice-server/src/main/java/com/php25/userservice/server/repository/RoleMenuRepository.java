package com.php25.userservice.server.repository;

import com.php25.userservice.server.model.RoleMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Zhangbing on 2017/4/24.
 */
@Repository
public interface RoleMenuRepository extends com.php25.common.jpa.repository.BaseRepository<RoleMenu, Long> {

    @Query("from RoleMenu r where r.adminRole.id=:roleId and r.adminMenuButton.id=:menuId")
    RoleMenu findOneByRoleIdAndMenuId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}
