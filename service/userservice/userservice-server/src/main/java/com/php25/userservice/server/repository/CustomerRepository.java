package com.php25.userservice.server.repository;

import com.php25.common.jpa.repository.BaseRepository;
import com.php25.userservice.server.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by penghuiping on 16/9/2.
 */
@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {

    Customer findByUsernameAndPassword(@Param("name") String name, @Param("password") String password);

    Customer findOneByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);

    Customer findOneByPhone(String phone);

    Customer findOneByUidAndType(@Param("phone") String uid, @Param("type") Integer type);

    Customer findOneByWx(@Param("wx") String wx);

    Customer findOneByQQ(@Param("qq") String qq);

    Customer findOneBySina(@Param("weibo") String weibo);

    @Query("from Customer c where c.username like CONCAT('%',:name,'%') and c.enable=1")
    List<Customer> findByName(@Param("name") String name);
}
