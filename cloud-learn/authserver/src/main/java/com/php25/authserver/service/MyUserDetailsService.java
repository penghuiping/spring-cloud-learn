package com.php25.authserver.service;

import com.google.common.collect.Lists;
import com.php25.common.flux.ApiErrorCode;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.StringBo;
import com.php25.usermicroservice.client.bo.res.CustomerBoRes;
import com.php25.usermicroservice.client.rpc.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author: penghuiping
 * @date: 2019/3/28 17:24
 * @description:
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    CustomerService customerRpc;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StringBo stringBo = new StringBo();
        stringBo.setContent(username);
        Mono<CustomerBoRes> customerBoResultDto = customerRpc.findCustomerByMobile(Mono.just(stringBo));
        CustomerBoRes customerBoRes = customerBoResultDto.blockOptional().get();
        if (customerBoRes.getErrorCode() == ApiErrorCode.ok.value) {
            CustomerBo customerBo = customerBoRes.getReturnObject();
            return new User(customerBo.getUsername(), customerBo.getPassword(), Lists.newArrayList());
        } else {
            return User.builder().disabled(true).build();
        }
    }
}
