package com.joinsoft.userservice.server.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.userservice.server.model.AdminMenuButton;
import com.joinsoft.userservice.server.model.AdminRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by penghuiping on 1/20/15.
 */
@Repository
public interface AdminMenuButtonRepository extends BaseRepository<AdminMenuButton, String> {

    @Query("from AdminMenuButton a where a.parent is null and a.enable!=2 order by a.sort asc")
    List<AdminMenuButton> findRootMenus();

    @Query("from AdminMenuButton a where a.parent=:parent and a.enable!=2")
    List<AdminMenuButton> findMenusByParent(@Param("parent") AdminMenuButton parent);

    @Query("select b from AdminRole a join a.adminMenuButtons b with a=:role and  a.enable!=2")
    List<AdminMenuButton> findMenusByRole(@Param("role") AdminRole role);

    @Query("from AdminMenuButton a where a.parent is null and a.enable=1")
    List<AdminMenuButton> findRootMenusEnabled();

    @Query("from AdminMenuButton a where a.parent=:parent and a.enable=1 order by a.sort asc")
    List<AdminMenuButton> findMenusEnabledByParent(@Param("parent") AdminMenuButton parent);

    @Query("select b from AdminRole a join a.adminMenuButtons b with a=:role and a.enable=1")
    List<AdminMenuButton> findMenusEnabledByRole(@Param("role") AdminRole role);

    @Query("select b from AdminRole a join a.adminMenuButtons b with a=:role and a.enable=1 where b.parent=:parent")
    List<AdminMenuButton> findMenusEnabledByParentAndRole(@Param("parent") AdminMenuButton parent, @Param("role") AdminRole role);

    @Query("select max(a.sort) from AdminMenuButton a where a.enable=1")
    Integer findMenusMaxSort();
}
