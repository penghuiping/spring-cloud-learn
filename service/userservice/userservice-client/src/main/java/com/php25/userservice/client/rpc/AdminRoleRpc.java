package com.php25.userservice.client.rpc;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.userservice.client.dto.AdminRoleDto;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 后台角色操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 10:58
 * @description:
 */
public interface AdminRoleRpc {

    /**
     * 后台角色分页查询
     *
     * @param searchParamBuilder 搜索参数创建器
     * @param pageNum            当前第几页
     * @param pageSize           一页多少数据
     * @param sort               排序
     * @return
     */
    ResultDto<DataGridPageDto<AdminRoleDto>> query(SearchParamBuilder searchParamBuilder, Integer pageNum, Integer pageSize, Sort sort);

    /**
     * 新增或者更新后台管理角色
     *
     * @param adminRoleDto 后台管理角色
     * @return 新增或者更新后的管理角色
     */
    ResultDto<AdminRoleDto> save(AdminRoleDto adminRoleDto);

    /**
     * 软删除后台管理角色id
     *
     * @param ids 需要删除的后台管理角色id
     * @return true:软删除成,false:软删除失败
     */
    Boolean softDelete(List<Long> ids);


}
