package com.php25.userservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.Customer;
import com.php25.userservice.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Customer findByUsernameAndPassword(String username, String password) {
        return db.cnd(Customer.class).whereEq("username", username).andEq("password", password).andEq("enable", 1).single();
    }

    @Transactional
    @Override
    public Customer findOneByPhoneAndPassword(String phone, String password) {
        return db.cnd(Customer.class).whereEq("mobile", phone).andEq("password", password).andEq("enable", 1).single();
    }


    @Transactional
    @Override
    public Customer findOneByPhone(String phone) {
        return db.cnd(Customer.class).whereEq("mobile", phone).andEq("enable", 1).single();
    }

    @Override
    public List<Customer> findByName(String name) {
        return db.cnd(Customer.class).whereLike("username", "%" + name + "%").andEq("enable", 1).select();
    }
}
