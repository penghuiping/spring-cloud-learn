package com.php25.usermicroservice.server.repository;

import com.php25.common.jdbc.repository.BaseRepository;
import com.php25.userservice.server.model.Customer;

import java.util.List;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
public interface CustomerRepository extends BaseRepository<Customer, Long> {

    /**
     * 根据用户名与密码查询前台用户
     *
     * @param name     用户名
     * @param password 密码
     * @return 前台用户信息
     */
    Customer findByUsernameAndPassword(String name, String password);

    /**
     * 根据手机与密码查询前台用户信息
     *
     * @param phone    手机
     * @param password 密码
     * @return 前台用户信息
     */
    Customer findOneByMobileAndPassword(String phone, String password);

    /**
     * 根据邮箱与密码查询前台用户信息
     *
     * @param email    邮箱
     * @param password 密码
     * @return 前台用户信息
     */
    Customer findOneByEmailAndPassword(String email, String password);

    /**
     * 根据手机查询前台用户信息
     *
     * @param mobile 手机
     * @return 前台用户信息
     */
    Customer findOneByMobile(String mobile);

    /**
     * 根据用户名进行模糊查询
     *
     * @param name 用户名
     * @return 匹配到的前台用户列表信息
     */
    List<Customer> findByUsername(String name);

    /**
     * 根据邮箱查询前台用户信息
     *
     * @param email 邮箱
     * @return 前台用户信息
     */
    Customer findByEmail(String email);


}
