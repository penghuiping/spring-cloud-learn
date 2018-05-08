package com.php25.userservice.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.specification.Operator;
import com.php25.common.specification.SearchParam;
import com.php25.userservice.server.dto.AdminMenuButtonDto;
import com.php25.userservice.server.dto.AdminRoleDto;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminMenuServiceTest extends BaseServiceTest {

    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private AdminRoleService adminRoleService;


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
        adminMenuService.save(adminMenuButtonDto);
        adminMenuService.save(adminMenuButtonDto1);

        //findMenusByParent
        Optional<List<AdminMenuButtonDto>> adminMenuButtonDtos = adminMenuService.findMenusByParent(adminMenuButtonDto);

        Assert.assertEquals(adminMenuButtonDtos.get().get(0).getId(), adminMenuButtonDto1.getId());

        AdminRoleDto adminRoleDto = new AdminRoleDto();
        adminRoleDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminRoleDto.setCreateTime(new Date());
        adminRoleDto.setUpdateTime(new Date());
        adminRoleDto.setEnable(1);
        adminRoleDto.setCode("sys-admin");
        adminRoleDto.setName("系统管理员");
        adminRoleDto.setDescription("系统管理员");
        adminRoleDto.setMenus(Lists.newArrayList(adminMenuButtonDto, adminMenuButtonDto1));
        adminRoleService.save(adminRoleDto);

        //findMenusByRole
        adminMenuButtonDtos = adminMenuService.findMenusByRole(adminRoleDto);
        Assert.assertTrue(adminMenuButtonDtos.get().size() == 2);

        //findMenusEnabledByParent
        adminMenuButtonDtos = adminMenuService.findMenusEnabledByParent(adminMenuButtonDto);
        Assert.assertEquals(adminMenuButtonDtos.get().get(0).getId(), adminMenuButtonDto1.getId());

        //findMenusEnabledByParentAndRole
        adminMenuButtonDtos = adminMenuService.findMenusEnabledByParentAndRole(adminMenuButtonDto, adminRoleDto);
        Assert.assertEquals(adminMenuButtonDtos.get().get(0).getId(), adminMenuButtonDto1.getId());

        //findOne
        Optional<AdminMenuButtonDto> adminMenuButtonDto2 = adminMenuService.findOne(adminMenuButtonDto.getId());
        Assert.assertEquals(adminMenuButtonDto2.get().getId(), adminMenuButtonDto.getId());

        //findMenusEnabledByRole
        adminMenuButtonDtos = adminMenuService.findMenusEnabledByRole(adminRoleDto);

        Assert.assertTrue(adminMenuButtonDtos.get().size() == 2);

        //findRootMenus
        adminMenuButtonDtos = adminMenuService.findRootMenus();

        Assert.assertTrue(adminMenuButtonDtos.get().size() == 1);

        //findRootMenusEnabled
        adminMenuButtonDtos = adminMenuService.findRootMenusEnabled();

        Assert.assertTrue(adminMenuButtonDtos.get().size() == 1);

        //query
        Optional<DataGridPageDto<AdminMenuButtonDto>> adminMenuButtonDtoDataGridPageDto = adminMenuService.query(1, 1, "[]");

        Assert.assertTrue(adminMenuButtonDtoDataGridPageDto.get().getData().size() == 1);

        //softDelete
        adminMenuService.softDelete(Lists.newArrayList(adminMenuButtonDto, adminMenuButtonDto1));

        SearchParam searchParam = new SearchParam();
        searchParam.setFieldName("enable");
        searchParam.setOperator(Operator.EQ.name());
        searchParam.setValue(1);
        adminMenuButtonDtoDataGridPageDto = adminMenuService.query(1, 10, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        Assert.assertTrue(adminMenuButtonDtoDataGridPageDto.get().getData().size() == 0);


    }
}
