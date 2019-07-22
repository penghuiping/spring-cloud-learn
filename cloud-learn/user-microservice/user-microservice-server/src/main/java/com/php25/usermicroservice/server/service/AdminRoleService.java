package com.php25.usermicroservice.server.service;


import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.service.ModelToDtoTransferable;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.model.AdminRole;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminRoleService extends InitializingBean {

    Optional<AdminRoleDto> save(AdminRoleDto obj);

    Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids);

    Optional<List<AdminRoleDto>> findAll();

    Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, String searchParams);

    Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, SearchParamBuilder searchParamBuilder, ModelToDtoTransferable modelToDtoTransferable, Sort sort);

    Optional<AdminRoleDto> findOne(Long id);

    void softDelete(List<AdminRoleDto> adminRoleDtos);


    /**
     * 查询所有有效数据
     *
     * @return List<AdminRoleDto>
     */
    Optional<List<AdminRoleDto>> findAllEnabled();

    /**
     * 根据ids查询所有的角色，此方法支持懒加载，由于AdminRoleDto中存在menus属性，它是一个list，如果懒加载的话，此方法不会把这个list
     * 查询出来。
     *
     * @param ids  后台角色id集合
     * @param lazy true:懒加载,false:不是懒加载
     * @return List<AdminRoleDto>
     */
    Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids, Boolean lazy);

}
