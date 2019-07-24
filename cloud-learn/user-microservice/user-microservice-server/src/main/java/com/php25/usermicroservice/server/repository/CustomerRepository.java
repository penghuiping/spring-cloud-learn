package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.Customer;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("select * from userservice_customer where username=:username and password=:password")
    Optional<Customer> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("select * from userservice_customer where mobile=:mobile")
    Optional<Customer> findByMobile(@Param("mobile") String mobile);

    @Query("select * from userservice_customer where email=:email")
    Optional<Customer> findByEmail(@Param("email") String email);
}
