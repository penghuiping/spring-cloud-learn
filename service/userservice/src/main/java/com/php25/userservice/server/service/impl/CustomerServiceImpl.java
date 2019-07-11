package com.php25.userservice.server.service.impl;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.exception.ServiceException;
import com.php25.common.core.service.ModelToDtoTransferable;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.userservice.server.dto.CustomerDto;
import com.php25.userservice.server.model.Customer;
import com.php25.userservice.server.repository.CustomerRepository;
import com.php25.userservice.server.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author penghuiping
 * @date 2019-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = ServiceException.class)
public class CustomerServiceImpl implements CustomerService {


    private CustomerRepository customerRepository;

    private BaseServiceImpl<CustomerDto, Customer, Long> baseService;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        baseService = new BaseServiceImpl<>(customerRepository);
    }

    @Override
    public Optional<CustomerDto> findOne(Long id) {
        return baseService.findOne(id);
    }

    @Override
    public Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password) {
        Assert.hasText(username, "用户名不能为空");
        Assert.hasText(password, "密码不能为空");
        var customer = customerRepository.findByUsernameAndPassword(username, password);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password) {
        Assert.hasText(phone, "手机不能为空");
        Assert.hasText(password, "密码不能为空");
        var customer = customerRepository.findOneByMobileAndPassword(phone, password);
        return transOptional(customer);
    }


    @Override
    public Optional<CustomerDto> findOneByPhone(String phone) {
        Assert.hasText(phone, "手机不能为空");
        var customer = customerRepository.findOneByMobile(phone);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> save(CustomerDto obj) {
        Assert.notNull(obj, "CustomerDto不能为null");
        if (null == obj.getId()) {
            obj.setCreateTime(new Date());
        }
        obj.setUpdateTime(new Date());
        return new BaseServiceImpl<CustomerDto, Customer, Long>(customerRepository).save(obj);
    }


    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        return baseService.query(pageNum, pageSize, searchParams, BeanUtils::copyProperties, Sort.Direction.DESC, "id");
    }

    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, SearchParamBuilder searchParamBuilder, ModelToDtoTransferable modelToDtoTransferable, Sort sort) {
        return baseService.query(pageNum, pageSize, searchParamBuilder, modelToDtoTransferable, sort);
    }

    @Override
    public Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize) {
        var customerDtoDataGridPageDto = query(pageNum, pageSize, searchParams);
        if (customerDtoDataGridPageDto.isPresent()) {
            return Optional.ofNullable(customerDtoDataGridPageDto.get().getData());
        }
        return Optional.empty();
    }

    /**
     * 此方法用来转化对象
     *
     * @param customer
     * @return
     */
    private CustomerDto trans(Customer customer) {
        var customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    /**
     * model to dto
     *
     * @param customer
     * @return
     */
    private Optional<CustomerDto> transOptional(Customer customer) {
        if (null != customer) {
            var customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<CustomerDto>> findByUsername(String name) {
        Assert.hasText(name, "name不能为空");
        var customers = customerRepository.findByUsername(name);
        return Optional.of(customers.stream().map(this::trans).collect(Collectors.toList()));
    }

    @Override
    public Optional<CustomerDto> findOneByEmailAndPassword(String email, String password) {
        var customer = customerRepository.findOneByEmailAndPassword(email, password);
        if (null != customer) {
            var customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();

        }
    }

    @Override
    public Optional<CustomerDto> findOneByEmail(String email) {
        var customer = customerRepository.findByEmail(email);
        if (null != customer) {
            var customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();
        }
    }


}
