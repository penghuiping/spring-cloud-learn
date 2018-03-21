package com.joinsoft.userservice.client.rest;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.userservice.client.dto.AdminRoleDto;
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
public interface AdminRoleRest {

    /**
     * 查询所有有效数据
     *
     * @return
     */
    @RequestMapping(value = "/findAllEnabled")
    public List<AdminRoleDto> findAllEnabled();

    /**
     * 根据id获取角色详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findOne")
    public AdminRoleDto findOne(@NotBlank @RequestParam("id") String id);

    /**
     * 保存角色信息
     *
     * @param adminRoleDto
     * @return
     */
    @RequestMapping(value = "/save")
    public AdminRoleDto save(@NotNull @RequestBody AdminRoleDto adminRoleDto);

    /**
     * 根据id集合查询角色信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/findAll")
    public List<AdminRoleDto> findAll(@Size(min = 1) @RequestParam("ids") List<String> ids);

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/softDelete")
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
