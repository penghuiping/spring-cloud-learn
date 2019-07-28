package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.Role;
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
    @Query("update userservice_role a set a.enable=2 where a.id in (:ids)")
    Boolean softDelete(@Param("ids") List<Long> ids);

}
