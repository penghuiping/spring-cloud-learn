package com.php25.userservice.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.dto.AdminUserDto;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminUserServiceTest extends BaseServiceTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleService adminRoleService;

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
        adminRoleService.save(adminRoleDto);
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
        adminUserService.save(adminUserDto);

        //findOne
        Optional<AdminUserDto> adminUserDto1 = adminUserService.findOne(adminUserDto.getId());
        Assert.assertEquals(adminUserDto.getId(), adminUserDto1.get().getId());

        //findAll
        Optional<List<AdminUserDto>> adminUserDtoList = adminUserService.findAll(Lists.newArrayList(adminUserDto.getId()));
        Assert.assertEquals(adminUserDto.getId(), adminUserDtoList.get().get(0).getId());

        //findByUsernameAndPassword
        adminUserDto1 = adminUserService.findByLoginNameAndPassword("jack", "123456");
        Assert.assertEquals(adminUserDto.getId(), adminUserDto1.get().getId());

        adminUserDto1 = adminUserService.findByLoginNameAndPassword("jack1", "123456");
        Assert.assertNull(adminUserDto1);

        //query
        SearchParam searchParam = new SearchParam();
        searchParam.setFieldName("enable");
        searchParam.setOperator(Operator.EQ.name());
        searchParam.setValue(1);

        Optional<DataGridPageDto<AdminUserDto>> adminUserDtoDataGridPageDto = adminUserService.query(1, 1, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertNotNull(adminUserDtoDataGridPageDto.get().getData());
        Assert.assertEquals(adminUserDtoDataGridPageDto.get().getData().size(), 1);
        //softDelete
        adminUserService.softDelete(Lists.newArrayList(adminUserDto));
        adminUserDtoDataGridPageDto = adminUserService.query(1, 1, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertNotNull(adminUserDtoDataGridPageDto.get().getData());
        Assert.assertEquals(adminUserDtoDataGridPageDto.get().getData().size(), 0);

        logger.info("===============>:" + objectMapper.writeValueAsString(adminUserService.query(1, 10, "[]")));
    }
}
