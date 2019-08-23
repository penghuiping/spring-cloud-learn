package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.Role;
import io.netty.util.Constant;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long>, RoleExRepository {


    @Modifying
    @Query("update t_role a set a.enable=2 where a.id in (:ids) and a.app_id=:appId")
    Boolean softDelete(@Param("ids") List<Long> ids, @Param("appId") String appId);

    @Query("select * from t_role a where a.role_name=:name and app_id=:appId and a.enable=1")
    Optional<Role> findByNameAndAppId(@Param("name") String name,@Param("appId") String appId);

    @Modifying
    @Query("update t_role a set a.description=:description where a.id=:id and a.app_id=:appId")
    Boolean changeInfo(@Param("description") String description, @Param("id") Long id, @Param("appId") String appId);

}
