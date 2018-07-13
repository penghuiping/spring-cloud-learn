package com.php25.userservice.client.rest;

import com.php25.common.dto.DataGridPageDto;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
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
public interface AdminMenuRest {

    String baseUri = "/adminMenu";


    /**
     * 根据角色查询所有的有效菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestLine("GET " + baseUri + "/findMenusEnabledByRole?adminRoleId={adminRoleId}")
    public List<AdminMenuButtonDto> findMenusEnabledByRole(@NotBlank @Param("adminRoleId") String adminRoleId);

    /**
     * 根据父菜单与角色所有的有效菜单按钮
     *
     * @param parentId
     * @param adminRoleId
     * @return
     */
    @RequestLine("GET " + baseUri + "/findMenusEnabledByParentAndRole?parentId={parentId}&adminRoleId={adminRoleId}")
    public List<AdminMenuButtonDto> findMenusEnabledByParentAndRole(@NotBlank @Param("parentId") String parentId, @NotBlank @Param("adminRoleId") String adminRoleId);

    /**
     * 获取菜单按钮树状结构
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/findRootMenus")
    public List<AdminMenuButtonDto> findRootMenus();

    /**
     * 根据父菜单按钮查询字菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestLine("GET " + baseUri + "/findMenusByParent?parentId={parentId}")
    public List<AdminMenuButtonDto> findMenusByParent(@NotBlank @Param("parentId") String parentId);

    /**
     * 根据角色查询菜单按钮
     *
     * @param adminRoleId
     * @return
     */
    @RequestLine("GET " + baseUri + "/findMenusByRole?adminRoleId={adminRoleId}")
    public List<AdminMenuButtonDto> findMenusByRole(@NotBlank @Param("adminRoleId") String adminRoleId);

    /**
     * 获取有效的菜单按钮树状结构
     *
     * @return
     */
    @RequestLine("GET " + baseUri + "/findRootMenusEnabled")
    public List<AdminMenuButtonDto> findRootMenusEnabled();

    /**
     * 根据父菜单按钮获取所有有效的菜单按钮
     *
     * @param parentId
     * @return
     */
    @RequestLine("GET " + baseUri + "/findMenusEnabledByParent?parentId={parentId}")
    public List<AdminMenuButtonDto> findMenusEnabledByParent(@NotBlank @Param("parentId") String parentId);

    /**
     * 根据id获取菜单详情
     *
     * @param id
     * @return
     */
    @RequestLine("GET " + baseUri + "/findOne?id={id}")
    public AdminMenuButtonDto findOne(@NotBlank @Param("id") String id);

    /**
     * 保存菜单信息
     *
     * @param adminMenuButtonDto
     * @return
     */
    @RequestLine("POST " + baseUri + "/save")
    @Headers("Content-Type: application/json")
    public AdminMenuButtonDto save(@NotNull AdminMenuButtonDto adminMenuButtonDto);

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
