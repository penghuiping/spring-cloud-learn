package com.joinsoft.userservice.server.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.userservice.server.model.AdminRole;
import org.springframework.stereotype.Repository;

/**
 * Created by penghuiping on 1/20/15.
 */
@Repository
public interface AdminRoleRepository extends BaseRepository<AdminRole, String> {

}
