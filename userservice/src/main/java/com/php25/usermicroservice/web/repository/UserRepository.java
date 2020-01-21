package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.BaseDbRepository;
import com.php25.usermicroservice.web.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2015-01-19
 */
@Repository
public interface UserRepository extends BaseDbRepository<User, Long> {

    /**
     * 根据用户名与密码查询
     *
     * @param username
     * @param password
     * @return
     */
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 批量更新密码
     *
     * @param password 密码
     * @param ids      后台用户ids
     * @return true:更新成功，false:更新失败
     */
    Boolean updatePassword(@Param("password") String password, @Param("ids") List<Long> ids);

    Boolean softDelete(@Param("ids") List<Long> ids);

    Optional<User> findByUsername(@Param("username") String username);


    Optional<User> findByMobile(@Param("mobile") String mobile);

    Optional<User> findByEmail(@Param("email") String email);

    Boolean changUserBasicInfo(@Param("id") Long userId, @Param("nickname") String nickname, @Param("mobile") String mobile, @Param("email") String email, @Param("headImageId") String headImageId);

}
