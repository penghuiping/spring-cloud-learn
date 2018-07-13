package com.php25.userservice.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.client.rest.AdminMenuRest;
import com.php25.userservice.client.rest.AdminRoleRest;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class AdminMenuRestTest extends BaseRestTest {

    @Autowired
    private AdminMenuRest adminMenuRest;

    @Autowired
    private AdminRoleRest adminRoleRest;


    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void test() throws Exception {
        //save
        AdminMenuButtonDto adminMenuButtonDto = new AdminMenuButtonDto();
        adminMenuButtonDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminMenuButtonDto.setCreateTime(new Date());
        adminMenuButtonDto.setUpdateTime(new Date());
        adminMenuButtonDto.setDescription("系统管理");
        adminMenuButtonDto.setEnable(1);
        adminMenuButtonDto.setIsLeaf(false);
        adminMenuButtonDto.setIsMenu(true);
        adminMenuButtonDto.setName("系统管理");

        AdminMenuButtonDto adminMenuButtonDto1 = new AdminMenuButtonDto();
        adminMenuButtonDto1.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminMenuButtonDto1.setCreateTime(new Date());
        adminMenuButtonDto1.setUpdateTime(new Date());
        adminMenuButtonDto1.setDescription("用户管理");
        adminMenuButtonDto1.setEnable(1);
        adminMenuButtonDto1.setIsLeaf(false);
        adminMenuButtonDto1.setIsMenu(true);
        adminMenuButtonDto1.setName("用户管理");
        adminMenuButtonDto1.setParent(adminMenuButtonDto);
        adminMenuRest.save(adminMenuButtonDto);
        adminMenuRest.save(adminMenuButtonDto1);

        //findMenusByParent
        List<AdminMenuButtonDto> adminMenuButtonDtos = adminMenuRest.findMenusByParent(adminMenuButtonDto.getId().toString());

        Assert.assertEquals(adminMenuButtonDtos.get(0).getId(), adminMenuButtonDto1.getId());

        AdminRoleDto adminRoleDto = new AdminRoleDto();
        adminRoleDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminRoleDto.setCreateTime(new Date());
        adminRoleDto.setUpdateTime(new Date());
        adminRoleDto.setEnable(1);
        adminRoleDto.setCode("sys-admin");
        adminRoleDto.setName("系统管理员");
        adminRoleDto.setDescription("系统管理员");
        adminRoleDto.setMenus(Lists.newArrayList(adminMenuButtonDto, adminMenuButtonDto1));
        adminRoleRest.save(adminRoleDto);

        //findMenusByRole
        adminMenuButtonDtos = adminMenuRest.findMenusByRole(adminRoleDto.getId().toString());
        Assert.assertTrue(adminMenuButtonDtos.size() == 2);

        //findMenusEnabledByParent
        adminMenuButtonDtos = adminMenuRest.findMenusEnabledByParent(adminMenuButtonDto.getId().toString());
        Assert.assertEquals(adminMenuButtonDtos.get(0).getId(), adminMenuButtonDto1.getId());

        //findMenusEnabledByParentAndRole
        adminMenuButtonDtos = adminMenuRest.findMenusEnabledByParentAndRole(adminMenuButtonDto.getId().toString(), adminRoleDto.getId().toString());
        Assert.assertEquals(adminMenuButtonDtos.get(0).getId(), adminMenuButtonDto1.getId());

        //findOne
        AdminMenuButtonDto adminMenuButtonDto2 = adminMenuRest.findOne(adminMenuButtonDto.getId().toString());
        Assert.assertEquals(adminMenuButtonDto2.getId(), adminMenuButtonDto.getId());

        //findMenusEnabledByRole
        adminMenuButtonDtos = adminMenuRest.findMenusEnabledByRole(adminRoleDto.getId().toString());

        Assert.assertTrue(adminMenuButtonDtos.size() == 2);

        //findRootMenus
        adminMenuButtonDtos = adminMenuRest.findRootMenus();

        Assert.assertTrue(adminMenuButtonDtos.size() == 1);

        //findRootMenusEnabled
        adminMenuButtonDtos = adminMenuRest.findRootMenusEnabled();

        Assert.assertTrue(adminMenuButtonDtos.size() == 1);

        //query
        DataGridPageDto<AdminMenuButtonDto> adminMenuButtonDtoDataGridPageDto = adminMenuRest.query(1, 1, "[]");

        Assert.assertTrue(adminMenuButtonDtoDataGridPageDto.getData().size() == 1);

        //softDelete
        adminMenuRest.softDelete(Lists.newArrayList(adminMenuButtonDto.getId().toString(), adminMenuButtonDto1.getId().toString()));

        SearchParam searchParam = new SearchParam.Builder().fieldName("enable").operator(Operator.EQ).value(1).build();
        adminMenuButtonDtoDataGridPageDto = adminMenuRest.query(1, 10, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertTrue(adminMenuButtonDtoDataGridPageDto.getData().size() == 0);


    }
}
