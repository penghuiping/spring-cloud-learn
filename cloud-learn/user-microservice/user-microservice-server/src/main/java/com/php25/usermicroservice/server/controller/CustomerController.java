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
import com.php25.usermicroservice.server.dto.CustomerDto;
import com.php25.usermicroservice.server.service.CustomerService;
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
    private CustomerService customerService;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private TokenJwtService tokenJwtService;

    @Override
    @PostMapping("/register")
    public Mono<BooleanRes> register(@RequestBody Mono<CustomerBo> customerBoMono) {
        return customerBoMono.map(customerBo -> {
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(customerBo.getMobile());
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
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerBo, customerDto);
            Optional<CustomerDto> customerDtoOptional1 = customerService.save(customerDto);
            if (customerDtoOptional1.isPresent()) {
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
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByUsernameAndPassword(username, password);
            if (!optionalCustomerDto.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过用户名:%s与密码:%s找到对应的客户信息", username, password));
            }

            CustomerDto customerDto = optionalCustomerDto.get();
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
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByPhone(mobile);
            if (!optionalCustomerDto.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过手机号:%s找到相关数据", mobile));
            }

            CustomerDto customerDto = optionalCustomerDto.get();
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
            String code = loginByEmailBo.getCode();
            Optional<CustomerDto> optionalCustomerDto = customerService.findOneByEmailAndPassword(email, code);
            if (!optionalCustomerDto.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过邮箱:%s找到相关数据", email));
            }

            CustomerDto customerDto = optionalCustomerDto.get();
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
            Optional<CustomerDto> customerDtoOptional = customerService.findOne(customerId);

            if (!customerDtoOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException(String.format("无法通过customerId:%s找到对应的客户信息", customerId));
            }
            CustomerDto customerDto = customerDtoOptional.get();
            CustomerBo customerBo = new CustomerBo();
            BeanUtils.copyProperties(customerDto, customerBo);
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
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customerBo, customerDto);
            Optional<CustomerDto> customerDtoOptional = customerService.save(customerDto);
            if (!customerDtoOptional.isPresent()) {
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
            Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(mobile);
            if (customerDtoOptional.isPresent()) {
                CustomerDto customerDto = customerDtoOptional.get();
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
