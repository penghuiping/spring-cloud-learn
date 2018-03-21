package com.joinsoft.userservice.client.rest;

import com.joinsoft.common.dto.DataGridPageDto;
import com.joinsoft.userservice.client.dto.AdminUserDto;
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
public interface AdminUserRest {


    /**
     * 根据用户名与密码获取用户信息
     *
     * @param username
     * @param password
     * @return AdminUserDto
     * @author penghuiping
     * @Time 2016-08-12
     */
    @RequestMapping("/findByUsernameAndPassword")
    public AdminUserDto findByUsernameAndPassword(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password);

    /**
     * 根据id获取后台用户详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public AdminUserDto findOne(@NotBlank @RequestParam("id") String id);

    /**
     * 保存后台用户信息
     *
     * @param adminUserDto
     * @return
     */
    @RequestMapping("/save")
    public AdminUserDto save(@NotNull @RequestBody AdminUserDto adminUserDto);

    /**
     * 查询所有有效数据
     *
     * @param ids
     * @return
     */
    @RequestMapping("/findAll")
    public List<AdminUserDto> findAll(@Size(min = 1) @RequestParam("ids") List<String> ids);

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
    public DataGridPageDto<AdminUserDto> query(@Min(-1) @RequestParam("pageNum") Integer pageNum, @Min(1) @RequestParam("pageSize") Integer pageSize, @NotBlank @RequestParam("searchParams") String searchParams);
}
