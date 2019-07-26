package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.AdminRole;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface AdminRoleRepository extends PagingAndSortingRepository<AdminRole, Long>, AdminRoleExRepository {


    @Modifying
    @Query("update userservice_admin_role a set a.enable=2 where a.id in (:ids)")
    Boolean softDelete(@Param("ids") List<Long> ids);

}
