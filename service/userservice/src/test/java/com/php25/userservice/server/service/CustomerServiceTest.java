package com.php25.userservice.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.userservice.server.constant.CustomerUuidType;
import com.php25.userservice.server.dto.CustomerDto;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CustomerServiceTest extends BaseServiceTest {

    @Autowired
    private CustomerService customerRest;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() throws Exception {
        //创建
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        customerDto.setEnable(1);
        customerDto.setMobile("18812345678");
        customerDto.setWeibo("weibo123123");
        customerDto.setWx("wx123123");
        customerDto.setQq("qq123123");
        customerDto.setNickname("chongchongchong");
        customerDto.setPassword("123456123456");
        customerDto.setUsername("冲冲冲");
        customerDto.setCreateTime(new Date());
        Assert.assertNotNull(customerRest.save(customerDto));
        //findAll
        Optional<List<CustomerDto>> customerDtoList = customerRest.findAll();
        logger.info(objectMapper.writeValueAsString(customerDtoList));
        Assert.assertNotNull(customerDtoList);
        Assert.assertEquals(customerDtoList.get().get(0).getId(), customerDto.getId());

        //findByUuidAndType
        Optional<CustomerDto> customerDto1 = customerRest.findByUuidAndType("weibo123123", CustomerUuidType.weibo.value);
        Assert.assertNotNull(customerDto1);
        Assert.assertEquals(customerDto1.get().getId(), customerDto.getId());

        //findByName
        customerDtoList = customerRest.findByName("冲冲冲");
        Assert.assertNotNull(customerDtoList);
        Assert.assertEquals(customerDtoList.get().get(0).getId(), customerDto.getId());

        //findOne
        customerDto1 = customerRest.findOne(customerDto.getId());
        Assert.assertNotNull(customerDto1);
        Assert.assertEquals(customerDto1.get().getId(), customerDto.getId());

        //findOneByPhone
        customerDto1 = customerRest.findOneByPhone("18812345678");
        Assert.assertNotNull(customerDto1);
        Assert.assertEquals(customerDto1.get().getId(), customerDto.getId());

        //findOneByPhoneAndPassword
        customerDto1 = customerRest.findOneByPhoneAndPassword("18812345678", "123456123456");
        Assert.assertNotNull(customerDto1);
        Assert.assertEquals(customerDto1.get().getId(), customerDto.getId());

        //query
        customerDtoList = customerRest.query("[]", 1, 1);
        Assert.assertNotNull(customerDtoList);
        Assert.assertEquals(customerDtoList.get().get(0).getId(), customerDto.getId());

        //softDelete
        customerRest.softDelete(Lists.newArrayList(customerDto));
        SearchParam searchParam = new SearchParam.Builder().fieldName("enable").operator(Operator.EQ).value("1").build();
        customerDtoList = customerRest.query(objectMapper.writeValueAsString(Lists.newArrayList(searchParam)), 1, 1);
        logger.info(objectMapper.writeValueAsString(customerDtoList));
    }
}
