package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.Group;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/8/12 09:57
 * @description:
 */
public interface GroupRepository extends PagingAndSortingRepository<Group, Long>,GroupExRepository {

    @Modifying
    @Query("update t_group a set a.enable=2 where a.id in (:ids) and a.app_id=:appId")
    Boolean softDelete(@Param("ids") List<Long> ids, @Param("appId") String appId);

    @Modifying
    @Query("update t_group a set a.description=:description where a.id=:id and a.app_id=:appId")
    Boolean changeInfo(@Param("description") String description, @Param("id") Long id, @Param("appId") String appId);
}
