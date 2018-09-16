package com.php25.userservice.server.repository;

import com.php25.common.jdbc.repository.BaseRepository;
import com.php25.userservice.server.model.Customer;

import java.util.List;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
public interface CustomerRepository extends BaseRepository<Customer, Long> {

    Customer findByUsernameAndPassword(String name, String password);

    Customer findOneByPhoneAndPassword(String phone, String password);

    Customer findOneByPhone(String phone);

    Customer findOneByUidAndType(String uid, Integer type);

    Customer findOneByWx(String wx);

    Customer findOneByQQ(String qq);

    Customer findOneBySina(String weibo);

    List<Customer> findByName(String name);
}
