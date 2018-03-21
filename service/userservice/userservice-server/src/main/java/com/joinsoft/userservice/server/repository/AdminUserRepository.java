package com.joinsoft.userservice.server.repository;

import com.php25.common.repository.BaseRepository;
import com.joinsoft.userservice.server.model.AdminUser;
import org.springframework.stereotype.Repository;

/**
 * Created by penghuiping on 1/19/15.
 */
@Repository
public interface AdminUserRepository extends BaseRepository<AdminUser, String> {

    AdminUser findByLoginNameAndPassword(String username, String password);

}
