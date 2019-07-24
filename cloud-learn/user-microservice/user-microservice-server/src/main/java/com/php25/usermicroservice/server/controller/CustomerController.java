package com.php25.usermicroservice.server.controller;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.LoginByEmailBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.bo.ResetPwdByEmailBo;
import com.php25.usermicroservice.client.bo.ResetPwdByMobileBo;
import com.php25.usermicroservice.client.bo.StringBo;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.bo.res.CustomerBoRes;
import com.php25.usermicroservice.client.bo.res.StringRes;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import com.php25.usermicroservice.server.model.Customer;
import com.php25.usermicroservice.server.repository.CustomerRepository;
import com.php25.usermicroservice.server.service.TokenJwtService;
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
    private CustomerRepository customerRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private TokenJwtService tokenJwtService;

    @Override
    @PostMapping("/register")
    public Mono<BooleanRes> register(@RequestBody Mono<CustomerBo> customerBoMono) {
        return customerBoMono.map(customerBo -> {
            Optional<Customer> customerDtoOptional = customerRepository.findByMobile(customerBo.getMobile());
            if (customerDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("%s手机号在系统中已经存在,无法注册", customerDtoOptional.get().getMobile()));
            }

            //判断username是否存在，如果不存在，自动补上
            if (StringUtil.isBlank(customerBo.getUsername())) {
                customerBo.setUsername(idGeneratorService.getModelPrimaryKey());
            }
            //刚注册的用户都是合法用户
            customerBo.setEnable(1);
            //生成前台用户主键
            customerBo.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
            Customer customerDto = new Customer();
            BeanUtils.copyProperties(customerBo, customerDto);
            Customer customerDtoOptional1 = customerRepository.save(customerDto);
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
    @PostMapping("/loginByUsername")
    public Mono<StringRes> loginByUsername(@RequestBody Mono<LoginBo> loginBoMono) {
        return loginBoMono.map(loginBo -> {
            String username = loginBo.getUsername();
            String password = loginBo.getPassword();
            Optional<Customer> optionalCustomer = customerRepository.findByUsernameAndPassword(username, password);
            if (!optionalCustomer.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过用户名:%s与密码:%s找到对应的客户信息", username, password));
            }

            Customer customerDto = optionalCustomer.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            return tokenJwtService.getToken(customerDto.getId().toString(), map);
        }).map(jwt -> {
            StringRes stringRes = new StringRes();
            stringRes.setErrorCode(ApiErrorCode.ok.value);
            stringRes.setReturnObject(jwt);
            return stringRes;
        });
    }

    @Override
    @PostMapping("/loginByMobile")
    public Mono<StringRes> loginByMobile(@RequestBody Mono<LoginByMobileBo> loginByMobileBoMono) {
        return loginByMobileBoMono.map(loginByMobileBo -> {
            String mobile = loginByMobileBo.getMobile();
            Optional<Customer> optionalCustomer = customerRepository.findByMobile(mobile);
            if (!optionalCustomer.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过手机号:%s找到相关数据", mobile));
            }

            Customer customerDto = optionalCustomer.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            return tokenJwtService.getToken(customerDto.getId().toString(), map);
        }).map(jwt -> {
            StringRes stringRes = new StringRes();
            stringRes.setErrorCode(ApiErrorCode.ok.value);
            stringRes.setReturnObject(jwt);
            return stringRes;
        });
    }

    @Override
    @PostMapping("/loginByEmail")
    public Mono<StringRes> loginByEmail(@RequestBody Mono<LoginByEmailBo> loginByEmailBoMono) {
        return loginByEmailBoMono.map(loginByEmailBo -> {
            String email = loginByEmailBo.getEmail();
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            if (!optionalCustomer.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过邮箱:%s找到相关数据", email));
            }

            Customer customerDto = optionalCustomer.get();
            Map<String, Object> map = new HashMap<>();
            map.put("customer", customerDto);
            //生成jwt
            String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
            return jwt;
        }).map(jwt -> {
            StringRes stringRes = new StringRes();
            stringRes.setErrorCode(ApiErrorCode.ok.value);
            stringRes.setReturnObject(jwt);
            return stringRes;
        });
    }


    @Override
    @PostMapping("/resetPasswordByMobile")
    public Mono<BooleanRes> resetPasswordByMobile(@RequestBody Mono<ResetPwdByMobileBo> resetPwdByMobileBoMono) {
        return resetPwdByMobileBoMono.map(resetPwdByMobileBo -> {
            String mobile = resetPwdByMobileBo.getMobile();
            String newPassword = resetPwdByMobileBo.getNewPassword();
            Optional<Customer> customerDtoOptional = customerRepository.findByMobile(mobile);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            Customer customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            Customer newCustomerOptional = customerRepository.save(customerDto);
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
    public Mono<BooleanRes> resetPasswordByEmail(@RequestBody Mono<ResetPwdByEmailBo> resetPwdByEmailBoMono) {
        return resetPwdByEmailBoMono.map(resetPwdByEmailBo -> {
            String email = resetPwdByEmailBo.getEmail();
            String newPassword = resetPwdByEmailBo.getNewPassword();
            Optional<Customer> customerDtoOptional = customerRepository.findByEmail(email);
            if (!customerDtoOptional.isPresent()) {
                return false;
            }

            //重置密码
            Customer customerDto = customerDtoOptional.get();
            customerDto.setPassword(newPassword);
            Customer newCustomerOptional = customerRepository.save(customerDto);
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
    public Mono<CustomerBoRes> findOne(@RequestBody Mono<IdStringReq> jwtMono) {
        return jwtMono.map(idStringReq -> {
            String customerIdStr = tokenJwtService.getKeyByToken(idStringReq.getId());
            Long customerId = Long.valueOf(customerIdStr);
            Optional<Customer> customerDtoOptional = customerRepository.findById(customerId);

            if (!customerDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过customerId:%s找到对应的客户信息", customerId));
            }
            Customer customerDto = customerDtoOptional.get();
            CustomerBo customerBo = new CustomerBo();
            BeanUtils.copyProperties(customerDto, customerBo);
            customerBo.setJwt(idStringReq.getId());
            return customerBo;
        }).map(customerBo -> {
            CustomerBoRes customerBoRes = new CustomerBoRes();
            customerBoRes.setErrorCode(ApiErrorCode.ok.value);
            customerBoRes.setReturnObject(customerBo);
            return customerBoRes;
        });
    }

    @Override
    @PostMapping("/validateJwt")
    public Mono<BooleanRes> validateJwt(@RequestBody Mono<IdStringReq> jwtMono) {
        return jwtMono
                .map(idStringReq -> tokenJwtService.verifyToken(idStringReq.getId()))
                .map(aBoolean -> {
                    BooleanRes booleanRes = new BooleanRes();
                    booleanRes.setErrorCode(ApiErrorCode.ok.value);
                    booleanRes.setReturnObject(aBoolean);
                    return booleanRes;
                });
    }

    @Override
    @PostMapping("/update")
    public Mono<BooleanRes> update(@RequestBody Mono<CustomerBo> customerBoMono) {
        return customerBoMono.map(customerBo -> {
            Customer customerDto = new Customer();
            BeanUtils.copyProperties(customerBo, customerDto);
            Customer customerDtoOptional = customerRepository.save(customerDto);
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
    public Mono<CustomerBoRes> findCustomerByMobile(@RequestBody Mono<StringBo> mobileMono) {
        return mobileMono.map(stringBo -> {
            String mobile = stringBo.getContent();
            Optional<Customer> customerDtoOptional = customerRepository.findByMobile(mobile);
            if (customerDtoOptional.isPresent()) {
                Customer customerDto = customerDtoOptional.get();
                CustomerBo customerBo = new CustomerBo();
                BeanUtils.copyProperties(customerDto, customerBo);
                return customerBo;
            } else {
                throw Exceptions.throwIllegalStateException(String.format("无法通过mobile:%s找到对应的客户信息", mobile));
            }
        }).map(customerBo -> {
            CustomerBoRes customerBoRes = new CustomerBoRes();
            customerBoRes.setErrorCode(ApiErrorCode.ok.value);
            customerBoRes.setReturnObject(customerBo);
            return customerBoRes;
        });
    }

    @Override
    @PostMapping("/logout")
    public Mono<BooleanRes> logout(@RequestBody Mono<IdStringReq> jwtMono) {
        return jwtMono.map(idStringReq -> {
            if (!tokenJwtService.verifyToken(idStringReq.getId())) {
                log.info("jwt不合法,登出失败");
                return false;
            }
            return tokenJwtService.cleanToken(idStringReq.getId());
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
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
