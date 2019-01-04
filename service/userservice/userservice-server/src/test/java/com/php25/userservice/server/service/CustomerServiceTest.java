package com.php25.userservice.server.service;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.JsonUtil;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.server.CommonAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/9/5 10:40
 * @description:
 */
@Slf4j
@SpringBootTest(classes = {CommonAutoConfigure.class})
@RunWith(SpringRunner.class)
public class CustomerServiceTest {

    @Autowired
    @Resource
    private CustomerService customerService;

    @Test
    public void findOneByUsernameAndPassword() {
        Optional<CustomerDto> customerDtoOptional = customerService.findOneByUsernameAndPassword("jack", "123456");
        if (customerDtoOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(customerDtoOptional.get()));
        }
        Assert.assertTrue(customerDtoOptional.isPresent());
    }

    @Test
    public void findOneByPhoneAndPassword() {
        Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhoneAndPassword("18621287361", "123456");
        if (customerDtoOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(customerDtoOptional.get()));
        }
        Assert.assertTrue(customerDtoOptional.isPresent());
    }

    @Test
    public void findOneByPhone() {
        Optional<CustomerDto> customerDtoOptional = customerService.findOneByPhone("18621287361");
        if (customerDtoOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(customerDtoOptional.get()));
        }
        Assert.assertTrue(customerDtoOptional.isPresent());
    }

    @Test
    public void query() {
        Optional<DataGridPageDto<CustomerDto>> dataGridPageDtoOptional = customerService.query(1, 2,
                SearchParamBuilder.builder().append(SearchParam.of("username", Operator.EQ, "jack")),
                BeanUtils::copyProperties, Sort.by("id"));

        if (dataGridPageDtoOptional.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(dataGridPageDtoOptional.get().getData()));
        }
    }

    @Test
    public void findByName() {
        Optional<List<CustomerDto>> optionalCustomerList = customerService.findByUsername("jack");
        if (optionalCustomerList.isPresent()) {
            System.out.println(JsonUtil.toPrettyJson(optionalCustomerList.get()));
        }
    }
}
