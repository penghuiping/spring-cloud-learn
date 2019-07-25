package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.BaseRepository;
import com.php25.usermicroservice.server.model.RoleMenu;
import org.springframework.data.repository.query.Param;

/**
 * @author penghuiping
 * @date 2017/4/24
 */
public interface RoleMenuRepository extends BaseRepository<RoleMenu, Long> {

    RoleMenu findOneByRoleIdAndMenuId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}
