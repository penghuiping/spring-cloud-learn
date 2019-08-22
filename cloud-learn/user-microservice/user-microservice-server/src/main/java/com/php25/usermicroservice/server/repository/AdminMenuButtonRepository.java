package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.AdminMenuButton;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface AdminMenuButtonRepository extends PagingAndSortingRepository<AdminMenuButton, Long> {

    /**
     * 查出所有的1级菜单
     *
     * @return
     */
    @Query("select * from userservice_menu a where a.parent is null and a.enable!=2 order by a.sort asc")
    List<AdminMenuButton> findRootMenus();

    /**
     * 查出所有的有效的1级菜单
     *
     * @return
     */
    @Query("select * from userservice_menu a where a.parent is null and a.enable=1 order by a.sort asc")
    Optional<List<AdminMenuButton>> findRootMenusEnabled();


    /**
     * 查出所有有效的菜单
     *
     * @return
     */
    @Query("select * from userservice_menu a where a.enable=1 order by a.sort asc")
    Optional<List<AdminMenuButton>> findAllEnable();


    /**
     * 根据父级菜单的id查询出子菜单的id
     *
     * @param parentId
     * @return
     */
    @Query("select * from userservice_menu a where a.parent =:parent  and a.enable!=2")
    List<AdminMenuButton> findMenusByParent(@Param("parent") Long parentId);

    /**
     * 根据父级菜单的id查询出有效的子菜单的id
     *
     * @param parentId
     * @return
     */
    @Query("select * from userservice_menu a where a.parent =:parent  and a.enable=1")
    List<AdminMenuButton> findMenusEnabledByParent(@Param("parent") Long parentId);

    /**
     * 查询出菜单与按钮表中sort字段最大的值
     *
     * @return
     */
    @Query("select max(a.sort) from userservice_menu a where  a.enable=1")
    Integer findMenusMaxSort();
}
