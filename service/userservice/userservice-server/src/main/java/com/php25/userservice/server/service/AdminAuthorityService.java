package com.php25.userservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.userservice.client.dto.AdminAuthorityDto;
import com.php25.userservice.server.model.AdminAuthority;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:22
 * @description:
 */
public interface AdminAuthorityService extends BaseService<AdminAuthorityDto, AdminAuthority, Long>, SoftDeletable<AdminAuthorityDto> {

    public Optional<List<AdminAuthorityDto>> findAllByAdminMenuButtonIds(List<Long> ids);
}
