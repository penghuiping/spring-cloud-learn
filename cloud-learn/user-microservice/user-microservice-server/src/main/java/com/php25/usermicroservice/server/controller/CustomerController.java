package com.php25.usermicroservice.server.controller;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.LoginByEmailBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.bo.ResetPwdByEmailBo;
import com.php25.usermicroservice.client.bo.ResetPwdByMobileBo;
import com.php25.usermicroservice.client.bo.StringBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import com.php25.usermicroservice.server.dto.CustomerDto;
import com.php25.usermicroservice.server.mq.GreetingsService;
import com.php25.usermicroservice.server.service.CustomerService;
import com.php25.usermicroservice.server.service.TokenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/7/13 17:06
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerRpc {

    @Autowired
    GreetingsService greetingsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private TokenJwtService tokenJwtService;

    @Override
    @PostMapping("/register")
    public Mono<Boolean> register(@Valid Mono<CustomerBo> customerBoMono) {
        return customerBoMono.map(customerBo -> {
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(customerBo.getMobile());
            if (customerDtoOptional.isPresent()) {
                throw Exceptions.throwServiceException(String.format("%s手机号在系统中已经存在,无法注册", customerDtoOptional.get().getMobile()));
            }

            //判断username是否存在，如果不存在，自动补上
            if (StringUtil.isBlank(customerBo.getUsername())) {
                customerBo.setUsername(idGeneratorService.getModelPrimaryKey());
            }
            //刚注册的用户都是合法用户
            customerBo.setEnable(1);
            //生成前台用户主键
            customerBo.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerBo, customerDto);
            Optional<CustomerDto> customerDtoOptional1 = customerService.save(customerDto);
            if (customerDtoOptional1.isPresent()) {
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/loginByUsername")
    public Mono<String> loginByUsername(@Valid Mono<LoginBo> loginBoMono) {
        return loginBoMono.map(loginBo -> {
            String username = loginBo.getUsername();
            String password = loginBo.getPassword();
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByUsernameAndPassword(username, password);
            if (!optionalCustomerDto.isPresent()) {
                return null;
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            return tokenJwtService.getToken(customerDto.getId().toString(), map);
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/loginByMobile")
    public Mono<String> loginByMobile(@Valid Mono<LoginByMobileBo> loginByMobileBoMono) {
        return loginByMobileBoMono.map(loginByMobileBo -> {
            String mobile = loginByMobileBo.getMobile();
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByPhone(mobile);
            if (!optionalCustomerDto.isPresent()) {
                throw Exceptions.throwServiceException("无法通过手机号:" + mobile + "找到相关数据");
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
            return jwt;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/loginByEmail")
    public Mono<String> loginByEmail(@Valid Mono<LoginByEmailBo> loginByEmailBoMono) {
        return loginByEmailBoMono.map(loginByEmailBo -> {
            String email = loginByEmailBo.getEmail();
            String code = loginByEmailBo.getCode();
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByEmailAndPassword(email, code);
            if (!optionalCustomerDto.isPresent()) {
                throw Exceptions.throwServiceException("无法通过邮箱:" + email + "找到相关数据");
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
            return jwt;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }


    @Override
    @PostMapping("/resetPasswordByMobile")
    public Mono<Boolean> resetPasswordByMobile(@Valid Mono<ResetPwdByMobileBo> resetPwdByMobileBoMono) {
        return resetPwdByMobileBoMono.map(resetPwdByMobileBo -> {
            String mobile = resetPwdByMobileBo.getMobile();
            String newPassword = resetPwdByMobileBo.getNewPassword();
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(mobile);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            CustomerDto customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            Optional<CustomerDto> newCustomerDtoOptional = customerService.save(customerDto);
            if (!newCustomerDtoOptional.isPresent()) {
                return false;
            }
            return true;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/resetPasswordByEmail")
    public Mono<Boolean> resetPasswordByEmail(@Valid Mono<ResetPwdByEmailBo> resetPwdByEmailBoMono) {
        return resetPwdByEmailBoMono.map(resetPwdByEmailBo -> {
            String email = resetPwdByEmailBo.getEmail();
            String newPassword = resetPwdByEmailBo.getNewPassword();
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByEmail(email);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            CustomerDto customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            Optional<CustomerDto> newCustomerDtoOptional = customerService.save(customerDto);
            if (!newCustomerDtoOptional.isPresent()) {
                return false;
            }
            return true;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<CustomerBo> findOne(@Valid Mono<IdStringReq> jwtMono) {
        return jwtMono.map(idStringReq -> {
            String customerIdStr = tokenJwtService.getKeyByToken(idStringReq.getId());
            Long customerId = Long.valueOf(customerIdStr);
            Optional<CustomerDto> customerDtoOptional = customerService.findOne(customerId);

            if (!customerDtoOptional.isPresent()) {
                return null;
            }
            CustomerDto customerDto = customerDtoOptional.get();
            CustomerBo customerBo = new CustomerBo();
            BeanUtils.copyProperties(customerDto, customerBo);
            return customerBo;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });


    }

    @Override
    @PostMapping("/validateJwt")
    public Mono<Boolean> validateJwt(@Valid Mono<IdStringReq> jwtMono) {
        return jwtMono
                .map(idStringReq -> tokenJwtService.verifyToken(idStringReq.getId()))
                .doOnError(throwable -> {
                    log.error("出错啦", throwable);
                });
    }

    @Override
    @PostMapping("/update")
    public Mono<Boolean> update(@Valid Mono<CustomerBo> customerBoMono) {
        return customerBoMono.map(customerBo -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerBo, customerDto);
            Optional<CustomerDto> customerDtoOptional = customerService.save(customerDto);
            if (!customerDtoOptional.isPresent()) {
                return false;
            } else {
                return true;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/findCustomerByMobile")
    public Mono<CustomerBo> findCustomerByMobile(@Valid Mono<StringBo> mobileMono) {
        return mobileMono.map(stringBo -> {
            String mobile = stringBo.getContent();
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(mobile);
            if (customerDtoOptional.isPresent()) {
                CustomerDto customerDto = customerDtoOptional.get();
                CustomerBo customerBo = new CustomerBo();
                BeanUtils.copyProperties(customerDto, customerBo);
                return customerBo;
            } else {
                return null;
            }
        }).flatMap(s -> {
            if (null == s) {
                return Mono.empty();
            } else {
                return Mono.just(s);
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/logout")
    public Mono<Boolean> logout(@Valid Mono<IdStringReq> jwtMono) {
        return jwtMono.map(idStringReq -> {
            if (!tokenJwtService.verifyToken(idStringReq.getId())) {
                log.info("jwt不合法,登出失败");
                return false;
            }
            return tokenJwtService.cleanToken(idStringReq.getId());
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });

    }

    @Override
    public Mono<Object> testMessage() {
        return Mono.fromCallable(() -> {
            greetingsService.sendGreeting();
            return null;
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }
}
