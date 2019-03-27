package com.php25.usermicroservice.server.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.AssertUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import com.php25.userservice.server.dto.CustomerDto;
import com.php25.userservice.server.service.CustomerService;
import com.php25.userservice.server.service.TokenJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
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
    public Boolean register(CustomerBo customerBo) {
        //数据校验
        AssertUtil.notNull(customerBo, "customerDto不能为null");
        AssertUtil.hasText(customerBo.getMobile(), "customerDto中mobile字段不能为空");

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
    }

    @Override
    public ResultDto<String> loginByUsername(String username, String password) {
        //参数效验
        AssertUtil.hasText(username, "username用户名不能为空");
        AssertUtil.hasText(password, "password密码不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByUsernameAndPassword(username, password);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        Map<String, Object> map = new HashMap<>();
        map.put("customer", customerDto);
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
        return new ResultDto<>(true, jwt);
    }

    @Override
    public ResultDto<String> loginByMobile(String mobile) {
        //参数效验
        AssertUtil.hasText(mobile, "mobile手机号不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByPhone(mobile);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        Map<String, Object> map = new HashMap<>();
        map.put("customer", customerDto);
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
        return new ResultDto<>(true, jwt);
    }

    @Override
    public ResultDto<String> loginByEmail(String email, String password) {
        //参数效验
        AssertUtil.hasText(email, "email邮箱不能为空");
        AssertUtil.hasText(password, "password密码不能为空");

        Optional<CustomerDto> optionalCustomerDto = customerService.findOneByEmailAndPassword(email, password);
        if (!optionalCustomerDto.isPresent()) {
            return new ResultDto<>(false, "");
        }

        CustomerDto customerDto = optionalCustomerDto.get();
        Map<String, Object> map = new HashMap<>();
        map.put("customer", customerDto);
        //生成jwt
        String jwt = tokenJwtService.getToken(customerDto.getId().toString(), map);
        return new ResultDto<>(true, jwt);
    }

    @Override
    public Boolean resetPasswordByMobile(String mobile, String newPassword) {
        //参数效验
        AssertUtil.hasText(mobile, "mobile手机号参数不能为空");
        AssertUtil.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

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
        AssertUtil.hasText(email, "mobile手机号参数不能为空");
        AssertUtil.hasText(newPassword, "newPassword重置成的新密码参数不能为空");

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
    public ResultDto<CustomerBo> findOne(String jwt) {
        AssertUtil.hasText(jwt, "jwt参数不能为空");
        String customerIdStr = tokenJwtService.getKeyByToken(jwt);
        Long customerId = new Long(customerIdStr);
        Optional<CustomerDto> customerDtoOptional = customerService.findOne(customerId);

        if (!customerDtoOptional.isPresent()) {
            return new ResultDto<>(false, null);
        }
        CustomerDto customerDto = customerDtoOptional.get();
        CustomerBo customerBo = new CustomerBo();
        BeanUtils.copyProperties(customerDto, customerBo);
        return new ResultDto<>(true, customerBo);
    }

    @Override
    public Boolean validateJwt(String jwt) {
        AssertUtil.hasText(jwt, "jwt参数不能为空");
        return tokenJwtService.verifyToken(jwt);
    }

    @Override
    public Boolean update(CustomerBo customerBo) {
        //参数验证
        AssertUtil.notNull(customerBo, "customerBo不能为null");
        AssertUtil.notNull(customerBo.getId(), "customerBo.id不能为null");
        AssertUtil.hasText(customerBo.getMobile(), "customerBo.mobile不能为空");

        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customerBo, customerDto);
        Optional<CustomerDto> customerDtoOptional = customerService.save(customerDto);
        if (!customerDtoOptional.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ResultDto<CustomerBo> findCustomerDtoByMobile(String mobile) {
        AssertUtil.hasText(mobile, "mobile parameter can't be empty");
        Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone(mobile);
        if (customerDtoOptional.isPresent()) {
            CustomerDto customerDto = customerDtoOptional.get();
            CustomerBo customerBo = new CustomerBo();
            BeanUtils.copyProperties(customerDto, customerBo);
            return new ResultDto<>(true, customerBo);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public Boolean logout(String jwt) {
        AssertUtil.hasText(jwt, "jwt parameter can't be empty");
        if (!this.validateJwt(jwt)) {
            log.info("jwt不合法,登出失败");
            return false;
        }
        return tokenJwtService.cleanToken(jwt);
    }
}
