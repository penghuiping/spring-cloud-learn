package com.php25.userservice.client.rest;

import com.php25.common.dto.DataGridPageDto;
import com.php25.userservice.client.dto.AdminRoleDto;
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
public interface AdminRoleRest {

    String baseUri = "/adminRole";

    /**
     * 查询所有有效数据
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/findAllEnabled")
    public List<AdminRoleDto> findAllEnabled();

    /**
     * 根据id获取角色详情
     *
     * @param id
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOne?id={id}")
    public AdminRoleDto findOne(@NotBlank @Param("id") String id);

    /**
     * 保存角色信息
     *
     * @param adminRoleDto
     * @return
     */
    @RequestLine("POST " + baseUri + "/save")
    @Headers("Content-Type: application/json")
    public AdminRoleDto save(@NotNull AdminRoleDto adminRoleDto);

    /**
     * 根据id集合查询角色信息
     *
     * @param ids
     * @return
     */
    @RequestLine("GET " + baseUri + "/findAll?ids={ids}")
    public List<AdminRoleDto> findAll(@Size(min = 1) @Param("ids") List<String> ids);

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
    public DataGridPageDto query(@Min(-1) @Param("pageNum") Integer pageNum, @Min(1) @Param("pageSize") Integer pageSize, @NotBlank @Param("searchParams") String searchParams);
}
