package com.php25.userservice.client.rpc;


import com.php25.userservice.client.dto.AdminRoleDto;

import java.util.List;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminRoleRpc {

    /**
     * 查询所有有效数据
     *
     * @return
     */
    List<AdminRoleDto> findAllEnabled();


    List<AdminRoleDto> findAll(Iterable<Long> ids, Boolean lazy);

}
