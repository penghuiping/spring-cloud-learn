package com.joinsoft.userservice.server.rest;

import com.php25.common.dto.DataGridPageDto;
import com.joinsoft.userservice.client.dto.AdminRoleDto;
import com.joinsoft.userservice.client.rest.AdminRoleRest;
import com.joinsoft.userservice.server.service.AdminRoleService;
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
@RequestMapping("/adminRole")
public class AdminRoleRestImpl implements AdminRoleRest {

    @Autowired
    private AdminRoleService adminRoleService;


    /**
     * 查询所有有效数据
     *
     * @return
     */
    @RequestMapping(value = "/findAllEnabled")
    public List<AdminRoleDto> findAllEnabled() {
        return adminRoleService.findAllEnabled();
    }

    /**
     * 根据id获取角色详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findOne")
    public AdminRoleDto findOne(@NotBlank @RequestParam("id") String id) {
        return adminRoleService.findOne(id);
    }

    /**
     * 保存角色信息
     *
     * @param adminRoleDto
     * @return
     */
    @RequestMapping(value = "/save")
    public AdminRoleDto save(@NotNull @RequestBody AdminRoleDto adminRoleDto) {
        return adminRoleService.save(adminRoleDto);
    }

    /**
     * 根据id集合查询角色信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/findAll")
    public List<AdminRoleDto> findAll(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        return adminRoleService.findAll(ids);
    }

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        adminRoleService.softDelete(adminRoleService.findAll(ids, false));
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
        return adminRoleService.query(pageNum, pageSize, searchParams);
    }
}
