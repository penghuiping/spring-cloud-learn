package com.php25.usermicroservice.server.event;

import com.google.common.collect.Lists;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import com.php25.usermicroservice.server.model.Role;
import com.php25.usermicroservice.server.model.RoleRef;
import com.php25.usermicroservice.server.model.User;
import com.php25.usermicroservice.server.repository.RoleRepository;
import com.php25.usermicroservice.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/12 09:53
 * @description:
 */
@Component
@Slf4j
public class UserServiceListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "userservice.findByUsername", durable = "true"),
            exchange = @Exchange(value = "cloud-exchange", type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true", durable = "true"),
            key = "userservice.findByUsername")
    )
    @SendTo
    public Message findByUsername(@Payload String username) {
        log.info("进入查询username");
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                CustomerDtoRes customerDtoRes = new CustomerDtoRes();
                CustomerDto customerDto = new CustomerDto();
                BeanUtils.copyProperties(user, customerDto, "roles");
                if (null != user.getRoles() && !user.getRoles().isEmpty()) {
                    Set<Long> roleIds = user.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toSet());
                    Set<String> roleNames = Lists.newArrayList(roleRepository.findAllById(roleIds)).stream().map(Role::getName).collect(Collectors.toSet());
                    customerDto.setRoles(roleNames);
                }
                customerDtoRes.setErrorCode(ApiErrorCode.ok.value);
                customerDtoRes.setReturnObject(customerDto);
                log.info("customerDtoRes:{}", JsonUtil.toJson(customerDtoRes));
                return MessageBuilder.withPayload(JsonUtil.toJson(customerDtoRes)).build();
            } else {
                CustomerDtoRes customerDtoRes = new CustomerDtoRes();
                customerDtoRes.setErrorCode(ApiErrorCode.server_error.value);
                return MessageBuilder.withPayload(JsonUtil.toJson(customerDtoRes)).build();
            }
        } catch (Exception e) {
            CustomerDtoRes customerDtoRes = new CustomerDtoRes();
            customerDtoRes.setErrorCode(ApiErrorCode.server_error.value);
            return MessageBuilder.withPayload(JsonUtil.toJson(customerDtoRes)).build();
        }
    }
}
