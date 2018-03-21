package com.joinsoft.userservice.client.rest;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.userservice.client.dto.AdminMenuButtonDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by penghuiping on 2017/3/9.
 */
public interface AdminMenuRest {


    /**
     * 根据角色查询所有的有效菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusEnabledByRole")
    public List<AdminMenuButtonDto> findMenusEnabledByRole(@NotBlank @RequestParam("adminRoleId") String adminRoleId);

    /**
     * 根据父菜单与角色所有的有效菜单按钮
     *
     * @param parentId
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusEnabledByParentAndRole")
    public List<AdminMenuButtonDto> findMenusEnabledByParentAndRole(@NotBlank @RequestParam("parentId") String parentId, @NotBlank @RequestParam("adminRoleId") String adminRoleId);

    /**
     * 获取菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenus")
    public List<AdminMenuButtonDto> findRootMenus();

    /**
     * 根据父菜单按钮查询字菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findMenusByParent")
    public List<AdminMenuButtonDto> findMenusByParent(@NotBlank @RequestParam("parentId") String parentId);

    /**
     * 根据角色查询菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestMapping("/findMenusByRole")
    public List<AdminMenuButtonDto> findMenusByRole(@NotBlank @RequestParam("adminRoleId") String adminRoleId);

    /**
     * 获取有效的菜单按钮树状结构
     *
     * @return
     */
    @RequestMapping("/findRootMenusEnabled")
    public List<AdminMenuButtonDto> findRootMenusEnabled();

    /**
     * 根据父菜单按钮获取所有有效的菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findMenusEnabledByParent")
    public List<AdminMenuButtonDto> findMenusEnabledByParent(@NotBlank @RequestParam("parentId") String parentId);

    /**
     * 根据id获取菜单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public AdminMenuButtonDto findOne(@NotBlank @RequestParam("id") String id);

    /**
     * 保存菜单信息
     *
     * @param adminMenuButtonDto
     * @return
     */
    @RequestMapping("/save")
    public AdminMenuButtonDto save(@NotNull @RequestBody AdminMenuButtonDto adminMenuButtonDto);

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param searchParams
     * @return
     */
    @RequestMapping("/query")
    public DataGridPageDto query(@Min(-1) @RequestParam("pageNum") Integer pageNum, @Min(1) @RequestParam("pageSize") Integer pageSize, @NotBlank @RequestParam("searchParams") String searchParams);
}
