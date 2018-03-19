package com.joinsoft.userservice.repository;

import com.joinsoft.common.repository.BaseRepository;
import com.joinsoft.userservice.model.AdminUser;
import org.springframework.stereotype.Repository;

/**
 * Created by penghuiping on 1/19/15.
 */
@Repository
public interface AdminUserRepository extends BaseRepository<AdminUser, String> {

    AdminUser findByLoginNameAndPassword(String username, String password);

}
