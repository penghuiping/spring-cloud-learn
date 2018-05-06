package com.php25.userservice.server.service.impl;

import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecs;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by penghuiping on 16/9/2.
 */
@Transactional
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDto, Customer, String> implements CustomerService {


    private CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.baseRepository = customerRepository;
    }


    @Override
    public Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password) {
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
    }

    @Override
    public Optional<CustomerDto> findByUuidAndType(String uuid, Integer type) {
        Customer customer = null;
        if (type == CustomerUuidType.weixin.value) {
            customer = customerRepository.findOneByWx(uuid);
        } else if (type == CustomerUuidType.qq.value) {
            customer = customerRepository.findOneByQQ(uuid);
        } else if (type == CustomerUuidType.weixin.value) {
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
        Customer customer = customerRepository.findOneByPhoneAndPassword(phone, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
    }


    @Override
    public Optional<CustomerDto> findOneByPhone(String phone) {
        Customer customer = customerRepository.findOneByPhone(phone);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else return Optional.ofNullable(null);
    }

    @Override
    public Optional<CustomerDto> save(CustomerDto obj) {
        if (null != obj && null == obj.getId()) {
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
    public boolean softDel(List<String> ids) {
        if (null != ids && 0 < ids.size()) {
            List<CustomerDto> customerDtos = new ArrayList<>();
            for (String id : ids) {
                Optional<CustomerDto> customerDto = findOne(id);
                customerDtos.add(customerDto.get());
            }
            softDelete(customerDtos);
            return true;
        }
        return false;
    }


    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<Customer> customerPage = customerRepository.findAll(BaseSpecs.getSpecs(searchParams), pageRequest);
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
        List<Customer> customers = customerRepository.findByName(name);
        return Optional.ofNullable(customers.stream().map(customer -> trans(customer)).collect(Collectors.toList()));
    }
}
