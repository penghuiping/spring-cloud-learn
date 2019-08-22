package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.Role;
import io.netty.util.Constant;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long>, RoleExRepository {


    @Modifying
    @Query("update t_role a set a.enable=2 where a.id in (:ids) and a.app_id=:appId")
    Boolean softDelete(@Param("ids") List<Long> ids, @Param("appId") String appId);

    @Modifying
    @Query("update t_role a set a.description=:description where a.id=:id and a.app_id=:appId")
    Boolean changeInfo(@Param("description") String description, @Param("id") Long id, @Param("appId") String appId);

}
