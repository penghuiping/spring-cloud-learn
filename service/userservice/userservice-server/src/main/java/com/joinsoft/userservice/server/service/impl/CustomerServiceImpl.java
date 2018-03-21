package com.joinsoft.userservice.server.service.impl;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.common.service.impl.BaseServiceImpl;
import com.joinsoft.common.specification.BaseSpecs;
import com.joinsoft.userservice.client.constant.CustomerUuidType;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.server.model.Customer;
import com.joinsoft.userservice.server.repository.CustomerRepository;
import com.joinsoft.userservice.server.service.CustomerService;
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
import java.util.stream.Collectors;


/**
 * Created by penghuiping on 16/9/2.
 */
@Transactional
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDto, Customer> implements CustomerService {


    private CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.baseRepository = customerRepository;
    }


    @Override
    public CustomerDto findOneByUsernameAndPassword(String username, String password) {
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        } else return null;
    }

    @Override
    public CustomerDto findByUuidAndType(String uuid, Integer type) {
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
            return customerDto;
        } else return null;

    }

    @Override
    public CustomerDto findOneByPhoneAndPassword(String phone, String password) {
        Customer customer = customerRepository.findOneByPhoneAndPassword(phone, password);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        } else return null;
    }


    @Override
    public CustomerDto findOneByPhone(String phone) {
        Customer customer = customerRepository.findOneByPhone(phone);
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        } else return null;
    }

    @Override
    public CustomerDto save(CustomerDto obj) {
        if (null != obj && null == obj.getId()) {
            obj.setCreateTime(new Date());
        }
        obj.setUpdateTime(new Date());
        return super.save(obj);
    }

    @Override
    public List<CustomerDto> findAll() {
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
                CustomerDto customerDto = findOne(id);
                customerDtos.add(customerDto);
            }
            softDelete(customerDtos);
            return true;
        }
        return false;
    }


    @Override
    public DataGridPageDto<CustomerDto> query(Integer pageNum, Integer pageSize, String searchParams) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<Customer> customerPage = customerRepository.findAll(BaseSpecs.<Customer>getSpecs(searchParams), pageRequest);
        List<CustomerDto> customerDtos = customerPage.getContent().parallelStream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return customerDto;
        }).collect(Collectors.toList());

        PageImpl<CustomerDto> customerDtoPage = new PageImpl<CustomerDto>(customerDtos, null, customerPage.getTotalElements());
        return toDataGridPageDto(customerDtoPage);
    }

    @Override
    public List<CustomerDto> query(String searchParams, Integer pageNum, Integer pageSize) {
        DataGridPageDto<CustomerDto> customerDtoDataGridPageDto = query(pageNum, pageSize, searchParams);
        if (null != customerDtoDataGridPageDto) {
            return customerDtoDataGridPageDto.getData();
        }
        return null;
    }


    //此方法用来转化对象
    private CustomerDto trans(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public List<CustomerDto> findByName(String name) {
        List<Customer> customers = customerRepository.findByName(name);
        return customers.stream().map(customer -> trans(customer)).collect(Collectors.toList());
    }
}
