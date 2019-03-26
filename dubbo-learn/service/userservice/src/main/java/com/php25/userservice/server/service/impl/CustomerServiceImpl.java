package com.php25.userservice.server.service.impl;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.server.model.Customer;
import com.php25.userservice.server.repository.CustomerRepository;
import com.php25.userservice.server.repository.RoleMenuRepository;
import com.php25.userservice.server.repository.UserRoleRepository;
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
 * Created by penghuiping on 16/9/2.
 */
@Slf4j
@Transactional
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDto, Customer, Long> implements CustomerService {


    private CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.baseRepository = customerRepository;
    }


    @Override
    public Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password) {
        Assert.hasText(username, "用户名不能为空");
        Assert.hasText(password, "密码不能为空");
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password) {
        Assert.hasText(phone, "手机不能为空");
        Assert.hasText(password, "密码不能为空");
        Customer customer = customerRepository.findOneByMobileAndPassword(phone, password);
        return transOptional(customer);
    }


    @Override
    public Optional<CustomerDto> findOneByPhone(String phone) {
        Assert.hasText(phone, "手机不能为空");
        Customer customer = customerRepository.findOneByMobile(phone);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> save(CustomerDto obj) {
        Assert.notNull(obj, "CustomerDto不能为null");
        if (null == obj.getId()) {
            obj.setCreateTime(new Date());
        }
        obj.setUpdateTime(new Date());
        return super.save(obj);
    }


    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        return this.query(pageNum, pageSize, searchParams, BeanUtils::copyProperties, Sort.Direction.DESC, "id");
    }

    @Override
    public Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize) {
        Optional<DataGridPageDto<CustomerDto>> customerDtoDataGridPageDto = query(pageNum, pageSize, searchParams);
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
        CustomerDto customerDto = new CustomerDto();
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
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<CustomerDto>> findByUsername(String name) {
        Assert.hasText(name, "name不能为空");
        List<Customer> customers = customerRepository.findByUsername(name);
        return Optional.of(customers.stream().map(customer -> trans(customer)).collect(Collectors.toList()));
    }

    @Override
    public Optional<CustomerDto> findOneByEmailAndPassword(String email, String password) {
        Customer customer = customerRepository.findOneByEmailAndPassword(email, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();

        }
    }

    @Override
    public Optional<CustomerDto> findOneByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.of(customerDto);
        } else {
            return Optional.empty();
        }
    }


}
