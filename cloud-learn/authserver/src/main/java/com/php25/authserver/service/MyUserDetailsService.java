package com.php25.authserver.service;

import com.google.common.collect.Lists;
import com.php25.common.flux.ApiErrorCode;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/3/28 17:24
 * @description:
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StringDto stringBo = new StringDto();
        stringBo.setContent(username);
        String jwt = jwtService.generateJwt();
        stringBo.setJwt(jwt);
        Mono<CustomerDtoRes> customerBoResultDto = customerService.findCustomerByUsername(stringBo);
        CustomerDtoRes customerBoRes = customerBoResultDto.blockOptional(Duration.ofSeconds(5)).get();
        if (customerBoRes.getErrorCode() == ApiErrorCode.ok.value) {
            CustomerDto customerBo = customerBoRes.getReturnObject();
            if (null != customerBo.getRoles() && customerBo.getRoles().size() > 0) {
                List<GrantedAuthority> grantedAuthorities = customerBo.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                return new User(customerBo.getUsername(), customerBo.getPassword(), grantedAuthorities);

            } else {
                return new User(customerBo.getUsername(), customerBo.getPassword(), Lists.newArrayList());
            }


        } else {
            return User.builder().disabled(true).build();
        }
    }


}
