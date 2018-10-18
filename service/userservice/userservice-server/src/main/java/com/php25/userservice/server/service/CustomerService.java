package com.php25.userservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.server.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务类
 *
 * @author penghuiping
 * @Time 16/9/2.
 */
public interface CustomerService extends BaseService<CustomerDto, Customer, Long>, SoftDeletable<CustomerDto> {

    /**
     * 根据用户名与密码，查询出用户信息
     *
     * @param username
     * @param password
     * @return
     * @author penghuiping
     * @Time 16/9/2.
     */
    Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password);

    /**
     * 根据联系方式和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password);

    /**
     * 根据联系方式查询
     *
     * @param phone
     * @return
     */
    Optional<CustomerDto> findOneByPhone(String phone);


    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize);


    /**
     * 根据姓名模糊查询
     *
     * @param name
     * @return
     */
    Optional<List<CustomerDto>> findByName(String name);

}
