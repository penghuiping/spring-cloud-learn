package com.php25.userservice.server.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.StringUtil;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.rpc.CustomerRpc;
import com.php25.userservice.server.service.CustomerService;
import com.php25.userservice.server.service.TokenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/13 17:06
 * @Description:
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service
public class CustomerRpcImpl implements CustomerRpc {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private TokenJwtService tokenJwtService;

    @Override
    public Boolean register(CustomerDto customerDto) {
        //数据校验
        Assert.notNull(customerDto, "customerDto不能为null");
        Assert.hasText(customerDto.getMobile(), "customerDto中mobile字段不能为空");

        Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(customerDto.getMobile());
        if (customerDtoOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("%s手机号在系统中已经存在,无法注册", customerDtoOptional.get().getMobile()));
        }

        //判断username是否存在，如果不存在，自动补上
        if (StringUtil.isBlank(customerDto.getUsername())) {
            customerDto.setUsername(idGeneratorService.getModelPrimaryKey());
        }
        //刚注册的用户都是合法用户
        customerDto.setEnable(1);
        //生成前台用户主键
        customerDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());

        Optional<CustomerDto> customerDtoOptional1 = customerService.save(customerDto);
        if (customerDtoOptional1.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResultDto<String> loginByUsername(String username, String password) {
        //参数效验
        Assert.hasText(username, "username用户名不能为空");
        Assert.hasText(password, "password密码不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByUsernameAndPassword(username, password);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString());
        return new ResultDto<>(true, jwt);
    }

    @Override
    public ResultDto<String> loginByMobile(String mobile) {
        //参数效验
        Assert.hasText(mobile, "mobile手机号不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByPhone(mobile);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString());
        return new ResultDto<>(true, jwt);
    }

    @Override
    public ResultDto<String> loginByEmail(String email, String password) {
        //参数效验
        Assert.hasText(email, "email邮箱不能为空");
        Assert.hasText(password, "password密码不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByEmailAndPassword(email, password);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString());
        return new ResultDto<>(true, jwt);
    }

    @Override
    public Boolean resetPasswordByMobile(String mobile, String newPassword) {
        //参数效验
        Assert.hasText(mobile, "mobile手机号参数不能为空");
        Assert.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

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
    }

    @Override
    public Boolean resetPasswordByEmail(String email, String newPassword) {
        //参数效验
        Assert.hasText(email, "mobile手机号参数不能为空");
        Assert.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

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
    }

    @Override
    public ResultDto<CustomerDto> findOne(String jwt) {
        Assert.hasText(jwt, "jwt参数不能为空");
        String customerIdStr = tokenJwtService.getKeyByToken(jwt);
        Long customerId = new Long(customerIdStr);
        Optional<CustomerDto> customerDtoOptional = customerService.findOne(customerId);

        if (!customerDtoOptional.isPresent()) {
            return new ResultDto<>(false, null);
        }
        CustomerDto customerDto = customerDtoOptional.get();
        return new ResultDto<>(true, customerDto);
    }

    @Override
    public Boolean validateJwt(String jwt) {
        Assert.hasText(jwt, "jwt参数不能为空");
        return tokenJwtService.verifyToken(jwt);
    }

    @Override
    public Boolean update(CustomerDto customerDto) {
        //参数验证
        Assert.notNull(customerDto, "customerDto不能为null");
        Assert.notNull(customerDto.getId(), "customerDto.id不能为null");
        Assert.hasText(customerDto.getMobile(), "customerDto.mobile不能为空");

        Optional<CustomerDto> customerDtoOptional = customerService.save(customerDto);
        if (!customerDtoOptional.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ResultDto<CustomerDto> findCustomerDtoByMobile(String mobile) {
        Assert.hasText(mobile, "mobile parameter can't be empty");
        Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(mobile);
        if (customerDtoOptional.isPresent()) {
            return new ResultDto<>(true, customerDtoOptional.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public Boolean logout(String jwt) {
        Assert.hasText(jwt, "jwt parameter can't be empty");
        if (!this.validateJwt(jwt)) {
            log.info("jwt不合法,登出失败");
            return false;
        }
        return tokenJwtService.cleanToken(jwt);
    }
}
