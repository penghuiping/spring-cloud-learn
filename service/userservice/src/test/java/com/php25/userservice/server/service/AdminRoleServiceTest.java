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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminRoleServiceTest extends BaseServiceTest {

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<AdminMenuButtonDto> lists = Lists.newArrayList();

    @Before
    public void pretest() {
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

        lists.add(adminMenuButtonDto);
        lists.add(adminMenuButtonDto1);
    }

    @Test
    public void test() throws Exception {
        //save
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        adminRoleDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        adminRoleDto.setCreateTime(new Date());
        adminRoleDto.setUpdateTime(new Date());
        adminRoleDto.setEnable(1);
        adminRoleDto.setCode("sys-admin");
        adminRoleDto.setName("系统管理员");
        adminRoleDto.setDescription("系统管理员");
        adminRoleDto.setMenus(lists);
        adminRoleService.save(adminRoleDto);

        //findAll
        Optional<List<AdminRoleDto>> adminRoleDtos1 = adminRoleService.findAll(Lists.newArrayList(adminRoleDto.getId()));

        Assert.assertNotNull(adminRoleDtos1);
        Assert.assertEquals(adminRoleDtos1.get().get(0).getId(), adminRoleDto.getId());

        //findEnabled
        adminRoleDtos1 = adminRoleService.findAllEnabled();
        Assert.assertEquals(adminRoleDtos1.get().get(0).getId(), adminRoleDto.getId());

        //findOne
        Optional<AdminRoleDto> adminRoleDto1 = adminRoleService.findOne(adminRoleDto.getId());
        Assert.assertEquals(adminRoleDto1.get().getId(), adminRoleDto.getId());

        //query
        Optional<DataGridPageDto<AdminRoleDto>> adminRoleDtoDataGridPageDto = adminRoleService.query(1, 1, "[]");
        Assert.assertEquals(adminRoleDtoDataGridPageDto.get().getData().get(0).getId(), adminRoleDto.getId());

        //softDelete
        adminRoleService.softDelete(Lists.newArrayList(adminRoleDto));

        SearchParam searchParam = new SearchParam.Builder().fieldName("enable").operator(Operator.EQ).value(1).build();
        adminRoleDtoDataGridPageDto = adminRoleService.query(1, 1, objectMapper.writeValueAsString(Lists.newArrayList(searchParam)));
        logger.info("==========>:" + objectMapper.writeValueAsString(adminRoleDtoDataGridPageDto));


    }
}
