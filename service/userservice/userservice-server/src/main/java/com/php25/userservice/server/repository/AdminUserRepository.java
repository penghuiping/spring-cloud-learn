package com.php25.userservice.server.repository;

import com.php25.common.repository.BaseRepository;
import com.php25.userservice.server.model.AdminUser;
import org.springframework.stereotype.Repository;

/**
 * Created by penghuiping on 1/19/15.
 */
@Repository
public interface AdminUserRepository extends BaseRepository<AdminUser, Long> {

    AdminUser findByLoginNameAndPassword(String username, String password);

}
