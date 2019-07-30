package com.php25.usermicroservice.server.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdLongReq;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.ResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.ResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
import com.php25.usermicroservice.client.service.CustomerService;
import com.php25.usermicroservice.server.constant.UserBusinessError;
import com.php25.usermicroservice.server.model.Role;
import com.php25.usermicroservice.server.model.RoleRef;
import com.php25.usermicroservice.server.model.User;
import com.php25.usermicroservice.server.repository.RoleRepository;
import com.php25.usermicroservice.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2018/7/13 17:06
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;


    @Override
    @PostMapping("/register")
    public Mono<BooleanRes> register(@RequestBody CustomerDto customerDto) {
        return Mono.just(customerDto).map(customerBo -> {
            Optional<User> customerDtoOptional = userRepository.findByMobile(customerBo.getMobile());
            if (customerDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("%s手机号在系统中已经存在,无法注册", customerDtoOptional.get().getMobile()));
            }

            //判断username是否存在，如果不存在，自动补上
            if (StringUtil.isBlank(customerBo.getUsername())) {
                customerBo.setUsername(idGeneratorService.getSnowflakeId().longValue() + "");
            }
            //刚注册的用户都是合法用户
            customerBo.setEnable(1);
            //生成前台用户主键
            customerBo.setId(idGeneratorService.getSnowflakeId().longValue());
            User user = new User();
            BeanUtils.copyProperties(customerBo, user);
            User customerDtoOptional1 = userRepository.save(user);
            if (customerDtoOptional1 != null) {
                return true;
            } else {
                return false;
            }
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/resetPasswordByMobile")
    public Mono<BooleanRes> resetPasswordByMobile(@RequestBody ResetPwdByMobileDto resetPwdByMobileDto) {
        return Mono.just(resetPwdByMobileDto).map(resetPwdByMobileBo -> {
            String mobile = resetPwdByMobileBo.getMobile();
            String newPassword = resetPwdByMobileBo.getNewPassword();
            Optional<User> customerDtoOptional = userRepository.findByMobile(mobile);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            User customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            User newCustomerOptional = userRepository.save(customerDto);
            if (newCustomerOptional != null) {
                return false;
            }
            return true;
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/resetPasswordByEmail")
    public Mono<BooleanRes> resetPasswordByEmail(@RequestBody ResetPwdByEmailDto resetPwdByEmailDto) {
        return Mono.just(resetPwdByEmailDto).map(resetPwdByEmailBo -> {
            String email = resetPwdByEmailBo.getEmail();
            String newPassword = resetPwdByEmailBo.getNewPassword();
            Optional<User> customerDtoOptional = userRepository.findByEmail(email);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            User customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            User newCustomerOptional = userRepository.save(customerDto);
            if (newCustomerOptional != null) {
                return false;
            }
            return true;
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<CustomerDtoRes> findOne(@RequestBody IdLongReq idLongReq) {
        return Mono.just(idLongReq).map(idLongReq1 -> {
            Long customerId = idLongReq1.getId();
            Optional<User> customerDtoOptional = userRepository.findById(customerId);

            if (!customerDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过customerId:%s找到对应的客户信息", customerId));
            }
            User user = customerDtoOptional.get();
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(user, customerDto);
            return customerDto;
        }).map(customerBo -> {
            CustomerDtoRes customerBoRes = new CustomerDtoRes();
            customerBoRes.setErrorCode(ApiErrorCode.ok.value);
            customerBoRes.setReturnObject(customerBo);
            return customerBoRes;
        });
    }


    @Override
    @PostMapping("/update")
    public Mono<BooleanRes> update(@RequestBody CustomerDto customerDto) {
        return Mono.just(customerDto).map(customerBo -> {
            User user = new User();
            BeanUtils.copyProperties(customerBo, user);
            User customerDtoOptional = userRepository.save(user);
            if (customerDtoOptional != null) {
                return false;
            } else {
                return true;
            }
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping("/findCustomerByMobile")
    public Mono<CustomerDtoRes> findCustomerByMobile(@RequestBody StringDto mobileParam) {
        return Mono.just(mobileParam).map(stringBo -> {
            String mobile = stringBo.getContent();
            Optional<User> customerDtoOptional = userRepository.findByMobile(mobile);
            if (customerDtoOptional.isPresent()) {
                User customerDto = customerDtoOptional.get();
                CustomerDto customerBo = new CustomerDto();
                BeanUtils.copyProperties(customerDto, customerBo);
                return customerBo;
            } else {
                throw Exceptions.throwIllegalStateException(String.format("无法通过mobile:%s找到对应的客户信息", mobile));
            }
        }).map(customerBo -> {
            CustomerDtoRes customerBoRes = new CustomerDtoRes();
            customerBoRes.setErrorCode(ApiErrorCode.ok.value);
            customerBoRes.setReturnObject(customerBo);
            return customerBoRes;
        });
    }

    @Override
    @PostMapping("/findCustomerByUsername")
    public Mono<CustomerDtoRes> findCustomerByUsername(@RequestBody StringDto username) {
        return Mono.just(username).map(stringDto -> {
            String username1 = stringDto.getContent();
            Optional<User> userOptional = userRepository.findByUsername(username1);
            if (!userOptional.isPresent()) {
                throw Exceptions.throwBusinessException(UserBusinessError.USER_NOT_FOUND);
            } else {
                User user = userOptional.get();
                return user;
            }
        }).map(user -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(user, customerDto, "roles");
            if (null != user.getRoles() && !user.getRoles().isEmpty()) {
                List<Long> roleIds = user.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
                Set<String> roleNames = Lists.newArrayList(roleRepository.findAllById(roleIds)).stream().map(Role::getName).collect(Collectors.toSet());
                customerDto.setRoles(roleNames);
            }

            CustomerDtoRes customerDtoRes = new CustomerDtoRes();
            customerDtoRes.setErrorCode(ApiErrorCode.ok.value);
            customerDtoRes.setReturnObject(customerDto);
            return customerDtoRes;
        }).doOnError(throwable -> {
            log.info("出错啦",throwable);
        });
    }

//    @Override
//    public Mono<Object> testMessage() {
//        return Mono.fromCallable(() -> {
//            greetingsService.sendGreeting();
//            return null;
//        }).doOnError(throwable -> {
//            log.error("出错啦", throwable);
//        });
//    }
}
