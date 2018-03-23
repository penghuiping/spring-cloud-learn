package com.joinsoft.userservice.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.service.impl.IdGeneratorServiceImpl;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.common.util.MD5Util;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by penghuiping on 2017/3/9.
 */
public class AdminMenuRestTest {

    private CustomerRest customerRest;

    private IdGeneratorService idGeneratorService;

    private ObjectMapper objectMapper;

    @Before
    public void before() {
        customerRest = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(CustomerRest.class, "http://localhost:21000");

        idGeneratorService = new IdGeneratorServiceImpl();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void save() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(idGeneratorService.getModelPrimaryKey());
        customerDto.setMobile("18811111111");
        customerDto.setPassword(MD5Util.getStringMD5("123456"));
        customerDto.setEnable(1);
        customerRest.save(customerDto);
    }

    @Test
    public void customerRegister() throws Exception {

    }

    @Test
    public void findOneByPhoneAndPassword() throws Exception {
        CustomerDto customerDto = customerRest.findOneByPhoneAndPassword("18811111111", "e10adc3949ba59abbe56e057f20f883e");
        System.out.println(objectMapper.writeValueAsString(customerDto));
    }


    @Test
    public void findAll() throws Exception {
        List<CustomerDto> customerDtoList = customerRest.findAll();
        System.out.println(objectMapper.writeValueAsString(customerDtoList));
    }

    @Test
    public void findOneByPhone() throws Exception {
        CustomerDto customerDto = customerRest.findOneByPhone("18812345678");
        System.out.println(objectMapper.writeValueAsString(customerDto));
    }

    @Test
    public void findOne() throws Exception {
        CustomerDto customerDto = customerRest.findOne("11111111111111111111111111111111");
        System.out.println(objectMapper.writeValueAsString(customerDto));
    }

    @Test
    public void query() throws Exception {
        SearchParam searchParam = new SearchParam();
        searchParam.setFieldName("enable");
        searchParam.setOperator(Operator.EQ.name());
        searchParam.setValue(1);
        List<CustomerDto> customerDtoList = customerRest.query(objectMapper.writeValueAsString(Lists.newArrayList(searchParam)),1,5);
        System.out.println(objectMapper.writeValueAsString(customerDtoList));
    }

    @Test
    public void findByUuidAndType() throws Exception {
    }

    @Test
    public void softDelete() throws Exception {
        customerRest.softDelete(Lists.newArrayList("ab9b4574ae4f4f5b8197177082c295c6"));
    }

    @Test
    public void findByName() throws Exception {
        List<CustomerDto> customerDtos = customerRest.findByName("123");
        System.out.println(objectMapper.writeValueAsString(customerDtos));
    }
}
