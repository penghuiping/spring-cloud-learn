package com.php25.authserver.service;

import com.google.common.collect.Lists;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Message message = rabbitMessagingTemplate.sendAndReceive("cloud-exchange", "userservice.findByUsername", MessageBuilder.withPayload(username).build());
        CustomerDtoRes customerBoRes = JsonUtil.fromJson(message.getPayload().toString(), CustomerDtoRes.class);
        if (ApiErrorCode.ok.value == customerBoRes.getErrorCode()) {
            CustomerDto customerBo = customerBoRes.getReturnObject();
            if (null != customerBo.getRoles() && customerBo.getRoles().size() > 0) {
                List<GrantedAuthority> grantedAuthorities = customerBo
                        .getRoles()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return new User(customerBo.getUsername(), customerBo.getPassword(), grantedAuthorities);

            } else {
                return new User(customerBo.getUsername(), customerBo.getPassword(), Lists.newArrayList());
            }
        } else {
            return User.builder().disabled(true).build();
        }

    }


}
