package com.php25.usermicroservice.server.controller;

import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.AssertUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import com.php25.userservice.server.dto.CustomerDto;
import com.php25.userservice.server.mq.GreetingsService;
import com.php25.userservice.server.service.CustomerService;
import com.php25.userservice.server.service.TokenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<Boolean> register(@RequestBody CustomerBo customerBo) {
        //数据校验
        AssertUtil.notNull(customerBo, "customerDto不能为null");
        AssertUtil.hasText(customerBo.getMobile(), "customerDto中mobile字段不能为空");

        return Mono.fromCallable(() -> {
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(customerBo.getMobile());
            if (customerDtoOptional.isPresent()) {
                throw new IllegalArgumentException(String.format("%s手机号在系统中已经存在,无法注册", customerDtoOptional.get().getMobile()));
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
    public Mono<String> loginByUsername(String username, String password) {
        //参数效验
        AssertUtil.hasText(username, "username用户名不能为空");
        AssertUtil.hasText(password, "password密码不能为空");

        return Mono.fromCallable(() -> {
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByUsernameAndPassword(username, password);
            if (!optionalCustomerDto.isPresent()) {
                return null;
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            return tokenJwtService.getToken(customerDto.getId().toString(), map);
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
    @PostMapping("/loginByMobile")
    public Mono<String> loginByMobile(String mobile, String code) {
        //参数效验
        AssertUtil.hasText(mobile, "mobile手机号不能为空");

        return Mono.fromCallable(() -> {
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByPhone(mobile);
            if (!optionalCustomerDto.isPresent()) {
                return null;
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
            return jwt;
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
    @PostMapping("/loginByEmail")
    public Mono<String> loginByEmail(String email, String code) {
        //参数效验
        AssertUtil.hasText(email, "email邮箱不能为空");
        AssertUtil.hasText(code, "code密码不能为空");

        return Mono.fromCallable(() -> {
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByEmailAndPassword(email, code);
            if (!optionalCustomerDto.isPresent()) {
                return null;
            }

            CustomerDto customerDto = optionalCustomerDto.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
            return jwt;
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
    @PostMapping("/sendSms")
    public Mono<String> sendSms(String mobile) {
        return null;
    }

    @Override
    @PostMapping("/sendEmailCode")
    public Mono<String> sendEmailCode(String email) {
        return null;
    }

    @Override
    @PostMapping("/resetPasswordByMobile")
    public Mono<Boolean> resetPasswordByMobile(String mobile, String newPassword, String code) {
        //参数效验
        AssertUtil.hasText(mobile, "mobile手机号参数不能为空");
        AssertUtil.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

        return Mono.fromCallable(() -> {
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
    public Mono<Boolean> resetPasswordByEmail(String email, String newPassword, String code) {
        //参数效验
        AssertUtil.hasText(email, "mobile手机号参数不能为空");
        AssertUtil.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

        return Mono.fromCallable(() -> {
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
    public Mono<CustomerBo> findOne(String jwt) {
        AssertUtil.hasText(jwt, "jwt参数不能为空");

        return Mono.fromCallable(() -> {
            String customerIdStr = tokenJwtService.getKeyByToken(jwt);
            Long customerId = new Long(customerIdStr);
            Optional<CustomerDto> customerDtoOptional = customerService.findOne(customerId);

            if (!customerDtoOptional.isPresent()) {
                return null;
            }
            CustomerDto customerDto = customerDtoOptional.get();
            CustomerBo customerBo = new CustomerBo();
            BeanUtils.copyProperties(customerDto, customerBo);
            return customerBo;
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
    @PostMapping("/validateJwt")
    public Mono<Boolean> validateJwt(String jwt) {
        AssertUtil.hasText(jwt, "jwt参数不能为空");
        return Mono.fromCallable(() -> {
            return tokenJwtService.verifyToken(jwt);
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/update")
    public Mono<Boolean> update(@RequestBody CustomerBo customerBo) {
        //参数验证
        AssertUtil.notNull(customerBo, "customerBo不能为null");
        AssertUtil.notNull(customerBo.getId(), "customerBo.id不能为null");
        AssertUtil.hasText(customerBo.getMobile(), "customerBo.mobile不能为空");

        return Mono.fromCallable(() -> {
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
    public Mono<CustomerBo> findCustomerByMobile(String mobile) {
        AssertUtil.hasText(mobile, "mobile parameter can't be empty");
        return Mono.fromCallable(() -> {
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
    public Mono<Boolean> logout(String jwt) {
        AssertUtil.hasText(jwt, "jwt parameter can't be empty");
        return Mono.fromCallable(() -> {
            if (!tokenJwtService.verifyToken(jwt)) {
                log.info("jwt不合法,登出失败");
                return false;
            }
            return tokenJwtService.cleanToken(jwt);
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });

    }

    @Override
    public Mono<Object> testMessage() {
        return Mono.fromCallable(() -> {
            greetingsService.sendGreeting();
            return null;
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
}
