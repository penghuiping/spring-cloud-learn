package com.joinsoft.userservice.client.rest;

import com.joinsoft.userservice.client.dto.AdminUserDto;
import com.php25.common.dto.DataGridPageDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by penghuiping on 2017/3/9.
 */
public interface AdminUserRest {

    String baseUri = "/adminUser";

    /**
     * 根据用户名与密码获取用户信息
     *
     * @param username
     * @param password
     * @return AdminUserDto
     * @author penghuiping
     * @Time 2016-08-12
     */
    @RequestLine("GET " + baseUri + "/findByUsernameAndPassword?username={username}&password={password}")
    public AdminUserDto findByUsernameAndPassword(@NotBlank @Param("username") String username, @NotBlank @Param("password") String password);

    /**
     * 根据id获取后台用户详情
     *
     * @param id
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOne?id={id}")
    public AdminUserDto findOne(@NotBlank @Param("id") String id);

    /**
     * 保存后台用户信息
     *
     * @param adminUserDto
     * @return
     */
    @RequestLine("POST " + baseUri + "/save")
    @Headers("Content-Type: application/json")
    public AdminUserDto save(@NotNull AdminUserDto adminUserDto);

    /**
     * 查询所有有效数据
     *
     * @param ids
     * @return
     */
    @RequestLine("GET " + baseUri + "/findAll?ids={ids}")
    public List<AdminUserDto> findAll(@Size(min = 1) @Param("ids") List<String> ids);

    /**
     * 批量软删除
     *
     * @param ids
     * @return
     */
    @RequestLine("GET " + baseUri + "/softDelete?ids={ids}")
    public Boolean softDelete(@Size(min = 1) @Param("ids") List<String> ids);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param searchParams
     * @return
     */
    @RequestLine("GET " + baseUri + "/query?pageNum={pageNum}&pageSize={pageSize}&searchParams={searchParams}")
    public DataGridPageDto<AdminUserDto> query(@Min(-1) @Param("pageNum") Integer pageNum, @Min(1) @Param("pageSize") Integer pageSize, @NotBlank @Param("searchParams") String searchParams);
}
