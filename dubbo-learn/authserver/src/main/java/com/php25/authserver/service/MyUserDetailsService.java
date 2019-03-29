package com.php25.authserver.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.php25.common.core.dto.ResultDto;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: penghuiping
 * @date: 2019/3/28 17:24
 * @description:
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Reference(check = false)
    CustomerRpc customerRpc;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResultDto<CustomerBo> customerBoResultDto = customerRpc.findCustomerDtoByMobile(username);
        if (customerBoResultDto.isStatus()) {
            CustomerBo customerBo = customerBoResultDto.getObject();
            return new User(customerBo.getUsername(), customerBo.getPassword(), Lists.newArrayList());
        } else {
            return User.builder().disabled(true).build();
        }
    }
}
