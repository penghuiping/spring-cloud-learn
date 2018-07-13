package com.php25.userservice.server.service;


import com.php25.common.service.BaseService;
import com.php25.common.service.SoftDeletable;
import com.php25.userservice.server.dto.AdminRoleDto;
import com.php25.userservice.server.model.AdminRole;

import java.util.List;
import java.util.Optional;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminRoleService extends BaseService<AdminRoleDto, AdminRole, Long>, SoftDeletable<AdminRoleDto> {

    /**
     * 查询所有有效数据
     *
     * @return
     */
    Optional<List<AdminRoleDto>> findAllEnabled();


    Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids, Boolean lazy);

}
