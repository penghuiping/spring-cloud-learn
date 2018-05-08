package com.php25.userservice.server.rest;

import com.php25.common.dto.DataGridPageDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.client.rest.AdminRoleRest;
import com.php25.userservice.server.service.AdminRoleService;
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
        return adminRoleService.findAllEnabled().orElse(null);
    }

    /**
     * 根据id获取角色详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findOne")
    public AdminRoleDto findOne(@NotBlank @RequestParam("id") String id) {
        return adminRoleService.findOne(Long.parseLong(id)).orElse(null);
    }

    /**
     * 保存角色信息
     *
     * @param adminRoleDto
     * @return
     */
    @RequestMapping(value = "/save")
    public AdminRoleDto save(@NotNull @RequestBody AdminRoleDto adminRoleDto) {
        return adminRoleService.save(adminRoleDto).orElse(null);
    }

    /**
     * 根据id集合查询角色信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/findAll")
    public List<AdminRoleDto> findAll(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        List<Long> ids_ = ids.stream().map(a -> Long.parseLong(a)).collect(Collectors.toList());
        return adminRoleService.findAll(ids_).orElse(null);
    }

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/softDelete")
    public Boolean softDelete(@Size(min = 1) @RequestParam("ids") List<String> ids) {
        List<Long> ids_ = ids.stream().map(a -> Long.parseLong(a)).collect(Collectors.toList());
        adminRoleService.softDelete(adminRoleService.findAll(ids_, false).orElse(null));
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
        return adminRoleService.query(pageNum, pageSize, searchParams).orElse(null);
    }
}
