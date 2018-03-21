package com.joinsoft.userservice.server.service;

import com.joinsoft.common.service.BaseService;
import com.joinsoft.common.service.SoftDeletable;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.server.model.Customer;

import java.util.List;

/**
 * 用户服务类
 *
 * @author penghuiping
 * @Time 16/9/2.
 */
public interface CustomerService extends BaseService<CustomerDto, Customer>, SoftDeletable<CustomerDto> {

    /**
     * 根据用户名与密码，查询出用户信息
     *
     * @param username
     * @param password
     * @return
     * @author penghuiping
     * @Time 16/9/2.
     */
    CustomerDto findOneByUsernameAndPassword(String username, String password);

    /**
     * 根据uuid  和 type，查询出用户信息
     * 根据uuid查询 uuid指的是 微信或者qq或者微博账号
     *
     * @param uuid
     * @param type
     * @return
     * @author penghuiping
     * @Time 16/9/2.
     */
    CustomerDto findByUuidAndType(String uuid, Integer type);

    /**
     * 根据联系方式和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    CustomerDto findOneByPhoneAndPassword(String phone, String password);

    /**
     * 根据联系方式查询
     *
     * @param phone
     * @return
     */
    CustomerDto findOneByPhone(String phone);


    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<CustomerDto> query(String searchParams, Integer pageNum, Integer pageSize);

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    public boolean softDel(List<String> ids);

    /**
     * 根据姓名模糊查询
     *
     * @param name
     * @return
     */
    List<CustomerDto> findByName(String name);

}
