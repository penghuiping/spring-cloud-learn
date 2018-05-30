package com.php25.userservice.server.service.impl;

import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecsFactory;
import com.php25.userservice.client.constant.CustomerUuidType;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.server.model.Customer;
import com.php25.userservice.server.repository.CustomerRepository;
import com.php25.userservice.server.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by penghuiping on 16/9/2.
 */
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
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
    }

    @Override
    public Optional<CustomerDto> findByUuidAndType(String uuid, Integer type) {
        Assert.hasText(uuid, "uuid表示wx,weibo,qq账号,不能为空");
        Assert.notNull(type, "type不能为null");
        Customer customer = null;
        if (type == CustomerUuidType.weixin.value) {
            customer = customerRepository.findOneByWx(uuid);
        } else if (type == CustomerUuidType.qq.value) {
            customer = customerRepository.findOneByQQ(uuid);
        } else if (type == CustomerUuidType.weibo.value) {
            customer = customerRepository.findOneBySina(uuid);
        }
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);

    }

    @Override
    public Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password) {
        Assert.hasText(phone, "手机不能为空");
        Assert.hasText(password, "密码不能为空");
        Customer customer = customerRepository.findOneByPhoneAndPassword(phone, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
    }


    @Override
    public Optional<CustomerDto> findOneByPhone(String phone) {
        Assert.hasText(phone, "手机不能为空");
        Customer customer = customerRepository.findOneByPhone(phone);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
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
    public Optional<List<CustomerDto>> findAll() {
        List<Customer> customers = customerRepository.findAllEnabled();
        List<CustomerDto> customerDtos = null;
        if (null != customers && 0 < customers.size()) {
            customerDtos = customers.stream().map(customer -> trans(customer)).collect(Collectors.toList());
        }
        return super.findAll();
    }


    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        Assert.notNull(pageNum, "pageNum不能为null");
        Assert.notNull(pageSize, "pageSize不能为null");
        Assert.hasText(searchParams, "searchParams不能为空，如没有搜索条件可以用[]");
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<Customer> customerPage = customerRepository.findAll(BaseSpecsFactory.getJpaInstance().getSpecs(searchParams), pageRequest);
        List<CustomerDto> customerDtos = customerPage.getContent().parallelStream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        }).collect(Collectors.toList());

        PageImpl<CustomerDto> customerDtoPage = new PageImpl<CustomerDto>(customerDtos, null, customerPage.getTotalElements());
        return Optional.ofNullable(toDataGridPageDto(customerDtoPage));
    }

    @Override
    public Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize) {
        Assert.notNull(pageNum, "pageNum不能为null");
        Assert.notNull(pageSize, "pageSize不能为null");
        Assert.hasText(searchParams, "searchParams不能为空，如没有搜索条件可以用[]");
        Optional<DataGridPageDto<CustomerDto>> customerDtoDataGridPageDto = query(pageNum, pageSize, searchParams);
        if (customerDtoDataGridPageDto.isPresent()) {
            return Optional.ofNullable(customerDtoDataGridPageDto.get().getData());
        }
        return Optional.ofNullable(null);
    }


    //此方法用来转化对象
    private CustomerDto trans(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public Optional<List<CustomerDto>> findByName(String name) {
        Assert.hasText(name, "name不能为空");
        List<Customer> customers = customerRepository.findByName(name);
        return Optional.ofNullable(customers.stream().map(customer -> trans(customer)).collect(Collectors.toList()));
    }
}
