package com.joinsoft.userservice.service;


import com.joinsoft.common.service.BaseService;
import com.joinsoft.common.service.SoftDeletable;
import com.joinsoft.userservice.dto.AdminRoleDto;
import com.joinsoft.userservice.model.AdminRole;

import java.util.List;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminRoleService extends BaseService<AdminRoleDto, AdminRole>, SoftDeletable<AdminRoleDto> {

    /**
     * 查询所有有效数据
     *
     * @return
     */
    List<AdminRoleDto> findAllEnabled();


    public List<AdminRoleDto> findAll(Iterable<String> ids, Boolean lazy);

    public AdminRoleDto findOne(String id);
}
