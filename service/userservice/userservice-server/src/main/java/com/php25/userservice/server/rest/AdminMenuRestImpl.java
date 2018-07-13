package com.php25.userservice.server.rest;

import com.php25.common.dto.DataGridPageDto;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.client.rest.AdminMenuRest;
import com.php25.userservice.server.service.AdminMenuService;
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
import java.util.stream.Collectors;

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
        adminRole.setId(Long.parseLong(adminRoleId));
        return adminMenuService.findMenusEnabledByRole(adminRole).orElse(null);
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
        parent.setId(Long.parseLong(parentId));

        AdminRoleDto adminRole = new AdminRoleDto();
        adminRole.setId(Long.parseLong(adminRoleId));
        return adminMenuService.findMenusEnabledByParentAndRole(parent, adminRole).orElse(null);
    }

    /**
     * 获取菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenus")
    public List<AdminMenuButtonDto> findRootMenus() {
        return adminMenuService.findRootMenus().orElse(null);
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
        parent.setId(Long.parseLong(parentId));
        return adminMenuService.findMenusByParent(parent).orElse(null);
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
        adminRoleDto.setId(Long.parseLong(adminRoleId));
        return adminMenuService.findMenusByRole(adminRoleDto).orElse(null);
    }

    /**
     * 获取有效的菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenusEnabled")
    public List<AdminMenuButtonDto> findRootMenusEnabled() {
        return adminMenuService.findRootMenusEnabled().orElse(null);
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
        parent.setId(Long.parseLong(parentId));
        return adminMenuService.findMenusEnabledByParent(parent).orElse(null);
    }

    /**
     * 根据id获取菜单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public AdminMenuButtonDto findOne(@NotBlank @RequestParam("id") String id) {
        return adminMenuService.findOne(Long.parseLong(id)).orElse(null);
    }

    /**
     * 保存菜单信息
     *
     * @param adminMenuButtonDto
     * @return
     */
    @RequestMapping("/save")
    public AdminMenuButtonDto save(@NotNull @RequestBody AdminMenuButtonDto adminMenuButtonDto) {
        return adminMenuService.save(adminMenuButtonDto).orElse(null);
    }

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        List<Long> ids_ = ids.stream().map(a -> Long.parseLong(a)).collect(Collectors.toList());
        List<AdminMenuButtonDto> adminMenuButtonDtos = adminMenuService.findAll(ids_).orElse(null);
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
        return adminMenuService.query(pageNum, pageSize, searchParams).orElse(null);
    }
}
