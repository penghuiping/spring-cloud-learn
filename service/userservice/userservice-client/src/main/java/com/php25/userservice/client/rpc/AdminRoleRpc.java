package com.php25.userservice.client.rpc;


import com.php25.userservice.client.dto.AdminRoleDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminRoleRpc {

    /**
     * 查询所有有效数据
     *
     * @return
     */
    Optional<List<AdminRoleDto>> findAllEnabled();


    Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids, Boolean lazy);

}
