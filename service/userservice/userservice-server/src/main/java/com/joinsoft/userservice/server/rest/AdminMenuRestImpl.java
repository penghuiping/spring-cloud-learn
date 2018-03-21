package com.joinsoft.userservice.server.rest;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.userservice.client.dto.AdminMenuButtonDto;
import com.joinsoft.userservice.client.dto.AdminRoleDto;
import com.joinsoft.userservice.client.rest.AdminMenuRest;
import com.joinsoft.userservice.server.service.AdminMenuService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by penghuiping on 2017/3/9.
 */
@Validated
@RestController
@RequestMapping("/adminMenu")
public class AdminMenuRestImpl implements AdminMenuRest {

    @Autowired
    private AdminMenuService adminMenuService;


    /**
     * 根据角色查询所有的有效菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusEnabledByRole")
    public List<AdminMenuButtonDto> findMenusEnabledByRole(@NotBlank @RequestParam("adminRoleId") String adminRoleId) {
        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(adminRoleId);
        return adminMenuService.findMenusEnabledByRole(adminRole);
    }

    /**
     * 根据父菜单与角色所有的有效菜单按钮
     *
     * @param parentId
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusEnabledByParentAndRole")
    public List<AdminMenuButtonDto> findMenusEnabledByParentAndRole(@NotBlank @RequestParam("parentId") String parentId, @NotBlank @RequestParam("adminRoleId") String adminRoleId) {
        AdminMenuButtonDto parent = new AdminMenuButtonDto();
        parent.setId(parentId);

        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(adminRoleId);
        return adminMenuService.findMenusEnabledByParentAndRole(parent, adminRole);
    }

    /**
     * 获取菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenus")
    public List<AdminMenuButtonDto> findRootMenus() {
        return adminMenuService.findRootMenus();
    }

    /**
     * 根据父菜单按钮查询字菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findMenusByParent")
    public List<AdminMenuButtonDto> findMenusByParent(@NotBlank @RequestParam("parentId") String parentId) {
        AdminMenuButtonDto parent = new AdminMenuButtonDto();
        parent.setId(parentId);
        return adminMenuService.findMenusByParent(parent);
    }

    /**
     * 根据角色查询菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusByRole")
    public List<AdminMenuButtonDto> findMenusByRole(@NotBlank @RequestParam("adminRoleId") String adminRoleId) {
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        adminRoleDto.setId(adminRoleId);
        return adminMenuService.findMenusByRole(adminRoleDto);
    }

    /**
     * 获取有效的菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenusEnabled")
    public List<AdminMenuButtonDto> findRootMenusEnabled() {
        return adminMenuService.findRootMenusEnabled();
    }

    /**
     * 根据父菜单按钮获取所有有效的菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findMenusEnabledByParent")
    public List<AdminMenuButtonDto> findMenusEnabledByParent(@NotBlank @RequestParam("parentId") String parentId) {
        AdminMenuButtonDto parent = new AdminMenuButtonDto();
        parent.setId(parentId);
        return adminMenuService.findMenusEnabledByParent(parent);
    }

    /**
     * 根据id获取菜单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public AdminMenuButtonDto findOne(@NotBlank @RequestParam("id") String id) {
        return adminMenuService.findOne(id);
    }

    /**
     * 保存菜单信息
     *
     * @param adminMenuButtonDto
     * @return
     */
    @RequestMapping("/save")
    public AdminMenuButtonDto save(@NotNull @RequestBody AdminMenuButtonDto adminMenuButtonDto) {
        return adminMenuService.save(adminMenuButtonDto);
    }

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        List<AdminMenuButtonDto> adminMenuButtonDtos = adminMenuService.findAll(ids);
        adminMenuService.softDelete(adminMenuButtonDtos);
        return true;
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param searchParams
     * @return
     */
    @RequestMapping("/query")
    public DataGridPageDto query(@Min(-1) @RequestParam("pageNum") Integer pageNum, @Min(1) @RequestParam("pageSize") Integer pageSize, @NotBlank @RequestParam("searchParams") String searchParams) {
        return adminMenuService.query(pageNum, pageSize, searchParams);
    }
}
