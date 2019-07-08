package com.php25.userservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.userservice.server.dto.AdminAuthorityDto;
import com.php25.userservice.server.model.AdminAuthority;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:22
 * @description:
 */
public interface AdminAuthorityService extends InitializingBean {

    public Optional<List<AdminAuthorityDto>> findAllByAdminMenuButtonIds(List<Long> ids);

    public Optional<Set<AdminAuthorityDto>> findAllDistinctByAdminMenuButtonIds(List<Long> ids);
}
