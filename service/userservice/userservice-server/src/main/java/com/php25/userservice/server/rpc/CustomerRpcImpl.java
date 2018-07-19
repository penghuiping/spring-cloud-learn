package com.php25.userservice.server.rpc;

import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.rpc.CustomerRpc;
import com.php25.userservice.server.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/13 17:06
 * @Description:
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service
public class CustomerRpcImpl implements CustomerRpc {

    @Autowired
    private CustomerService customerService;

    @Override
    public CustomerDto findOne(Long id) {
        return customerService.findOne(id).get();
    }

    @Override
    public CustomerDto findOneByUsernameAndPassword(String username, String password) {
        return customerService.findOneByUsernameAndPassword(username, password).get();
    }

    @Override
    public CustomerDto findByUuidAndType(String uuid, Integer type) {
        return customerService.findByUuidAndType(uuid, type).get();
    }

    @Override
    public CustomerDto findOneByPhoneAndPassword(String phone, String password) {
        return customerService.findOneByPhoneAndPassword(phone, password).get();
    }

    @Override
    public CustomerDto findOneByPhone(String phone) {
        return customerService.findOneByPhone(phone).get();
    }

    @Override
    public List<CustomerDto> query(String searchParams, Integer pageNum, Integer pageSize) {
        return customerService.query(searchParams, pageNum, pageSize).get();
    }

    @Override
    public List<CustomerDto> findByName(String name) {
        return customerService.findByName(name).get();
    }
}
