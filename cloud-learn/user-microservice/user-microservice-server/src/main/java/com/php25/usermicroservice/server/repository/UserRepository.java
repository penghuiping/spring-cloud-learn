package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2015-01-19
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserExRepository {

    /**
     * 根据用户名与密码查询
     *
     * @param username
     * @param password
     * @return
     */
    @Query("select * from userservice_user where username=:username and password=:password")
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 批量更新密码
     *
     * @param password 密码
     * @param ids      后台用户ids
     * @return true:更新成功，false:更新失败
     */
    @Modifying
    @Query("update userservice_user a set a.password=:password , a.update_time=NOW() where a.id in (:ids)")
    Boolean updatePassword(@Param("password") String password, @Param("ids") List<Long> ids);

    @Modifying
    @Query("update userservice_user a set a.enable=2 where a.id in (:ids)")
    Boolean softDelete(@Param("ids") List<Long> ids);

    @Query("select * from userservice_user where username=:username")
    Optional<User> findByUsername(@Param("username") String username);


    @Query("select * from userservice_user where mobile=:mobile")
    Optional<User> findByMobile(@Param("mobile") String mobile);

    @Query("select * from userservice_user where email=:email")
    Optional<User> findByEmail(@Param("email") String email);

}
