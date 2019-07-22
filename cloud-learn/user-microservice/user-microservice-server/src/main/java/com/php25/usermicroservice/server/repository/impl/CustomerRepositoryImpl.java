package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.usermicroservice.server.model.Customer;
import com.php25.usermicroservice.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于操作Customer实体类
 *
 * @author penghuiping
 * @date 2015-02-20
 */
@Repository
public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, Long> implements CustomerRepository {

    @Autowired
    private Db db;

    @Override
    public Customer findByUsernameAndPassword(String username, String password) {
        return db.cnd(Customer.class).whereEq("username", username).andEq("password", password).andEq("enable", 1).single();
    }

    @Override
    public Customer findOneByMobileAndPassword(String mobile, String password) {
        return db.cnd(Customer.class).whereEq("mobile", mobile).andEq("password", password).andEq("enable", 1).single();
    }

    @Override
    public Customer findOneByEmailAndPassword(String email, String password) {
        return db.cnd(Customer.class).whereEq("email", email).andEq("password", password).andEq("enable", 1).single();
    }


    @Override
    public Customer findOneByMobile(String mobile) {
        return db.cnd(Customer.class).whereEq("mobile", mobile).andEq("enable", 1).single();
    }

    @Override
    public List<Customer> findByUsername(String name) {
        return db.cnd(Customer.class).whereLike("username", name + "%").andEq("enable", 1).select();
    }

    @Override
    public Customer findByEmail(String email) {
        return db.cnd(Customer.class).whereEq("email", email).single();
    }
}
