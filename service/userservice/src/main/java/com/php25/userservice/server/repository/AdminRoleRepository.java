package com.php25.userservice.server.repository;

import com.php25.common.repository.BaseRepository;
import com.php25.userservice.server.model.AdminRole;
import org.springframework.stereotype.Repository;

/**
 * Created by penghuiping on 1/20/15.
 */
@Repository
public interface AdminRoleRepository extends BaseRepository<AdminRole, Long> {

}
