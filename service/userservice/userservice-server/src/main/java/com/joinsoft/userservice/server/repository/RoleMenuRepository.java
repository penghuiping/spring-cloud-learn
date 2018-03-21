package com.joinsoft.userservice.server.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.userservice.server.model.RoleMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Zhangbing on 2017/4/24.
 */
@Repository
public interface RoleMenuRepository extends BaseRepository<RoleMenu,String> {

    @Query("from RoleMenu r where r.adminRole.id=:roleId and r.adminMenuButton.id=:menuId")
    RoleMenu findOneByRoleIdAndMenuId(@Param("roleId") String roleId,@Param("menuId")String menuId);
}
