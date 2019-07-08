package com.php25.userservice.server.service;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.userservice.server.dto.CustomerDto;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务类
 *
 * @author penghuiping
 * @Time 16/9/2.
 */
public interface CustomerService extends InitializingBean {

    Optional<CustomerDto> findOne(Long id);

    Optional<CustomerDto> save(CustomerDto obj);

    Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams);

    /**
     * 根据用户名与密码，查询出用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 前台用户信息
     */
    Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password);

    /**
     * 根据联系方式和密码查询
     *
     * @param phone    手机
     * @param password 密码
     * @return 前台用户信息
     */
    Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password);

    /**
     * 根据邮箱与密码获取前台用户信息
     *
     * @param email    邮箱
     * @param password 密码
     * @return 前台用户信息
     */
    Optional<CustomerDto> findOneByEmailAndPassword(String email, String password);

    /**
     * 根据联系方式查询
     *
     * @param phone 手机
     * @return 前台用户信息
     */
    Optional<CustomerDto> findOneByPhone(String phone);


    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return 前台用户信息
     */
    Optional<CustomerDto> findOneByEmail(String email);

    /**
     * 分页查询
     *
     * @param searchParams 分页查询参数
     * @param pageNum      当前第几页
     * @param pageSize     每页多少条记录
     * @return 前台用户列表信息
     */
    Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize);


    /**
     * 根据姓名模糊查询
     *
     * @param name 姓名
     * @return
     */
    Optional<List<CustomerDto>> findByUsername(String name);


}
