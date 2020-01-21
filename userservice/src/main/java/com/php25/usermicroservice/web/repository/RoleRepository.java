package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.BaseDbRepository;
import com.php25.usermicroservice.web.model.Role;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface RoleRepository extends BaseDbRepository<Role, Long> {


    Boolean softDelete(@Param("ids") List<Long> ids, @Param("appId") String appId);

    Optional<Role> findByNameAndAppId(@Param("name") String name,@Param("appId") String appId);

    Boolean changeInfo(@Param("description") String description, @Param("id") Long id, @Param("appId") String appId);

}
