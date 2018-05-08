package com.php25.userservice.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.client.dto.AdminUserDto;
import com.php25.userservice.client.rest.AdminRoleRest;
import com.php25.userservice.client.rest.AdminUserRest;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

public class AdminUserRestTest extends BaseRestTest {

    @Autowired
    private AdminUserRest adminUserRest;

    @Autowired
    private AdminRoleRest adminRoleRest;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private AdminRoleDto adminRoleDto;

    @Before
    public void pretest() throws Exception {
        this.adminRoleDto = new AdminRoleDto();
        adminRoleDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminRoleDto.setCreateTime(new Date());
        adminRoleDto.setDescription("系统管理员");
        adminRoleDto.setName("系统管理员");
        adminRoleDto.setCode("sys-admin");
        adminRoleDto.setEnable(1);
        adminRoleDto.setUpdateTime(new Date());
        adminRoleRest.save(adminRoleDto);
    }

    @Test
    public void test() throws Exception {
        //save
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminUserDto.setCreateTime(new Date());
        adminUserDto.setUpdateTime(new Date());
        adminUserDto.setEmail("123456@qq.com");
        adminUserDto.setEnable(1);
        adminUserDto.setMobile("18812345678");
        adminUserDto.setNickname("jack");
        adminUserDto.setUsername("jack");
        adminUserDto.setPassword("123456");
        adminUserDto.setRoles(Lists.newArrayList(adminRoleDto));
        adminUserRest.save(adminUserDto);

        //findOne
        AdminUserDto adminUserDto1 = adminUserRest.findOne(adminUserDto.getId() + "");
        Assert.assertEquals(adminUserDto.getId(), adminUserDto1.getId());

        //findAll
        List<AdminUserDto> adminUserDtoList = adminUserRest.findAll(Lists.newArrayList(adminUserDto.getId() + ""));
        Assert.assertEquals(adminUserDto.getId(), adminUserDtoList.get(0).getId());

        //findByUsernameAndPassword
        adminUserDto1 = adminUserRest.findByUsernameAndPassword("jack", "123456");
        Assert.assertEquals(adminUserDto.getId(), adminUserDto1.getId());

        adminUserDto1 = adminUserRest.findByUsernameAndPassword("jack1", "123456");
        Assert.assertNull(adminUserDto1);

        //query
        SearchParam searchParam = new SearchParam();
        searchParam.setFieldName("enable");
        searchParam.setOperator(Operator.EQ.name());
        searchParam.setValue(1);

        DataGridPageDto<AdminUserDto> adminUserDtoDataGridPageDto = adminUserRest.query(1, 1, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertNotNull(adminUserDtoDataGridPageDto.getData());
        Assert.assertEquals(adminUserDtoDataGridPageDto.getData().size(), 1);
        //softDelete
        adminUserRest.softDelete(Lists.newArrayList(adminUserDto.getId() + ""));
        adminUserDtoDataGridPageDto = adminUserRest.query(1, 1, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertNotNull(adminUserDtoDataGridPageDto.getData());
        Assert.assertEquals(adminUserDtoDataGridPageDto.getData().size(), 0);

        logger.info("===============>:" + objectMapper.writeValueAsString(adminUserRest.query(1, 10, "[]")));
    }
}
