package com.php25.usermicroservice.server.service;

import com.php25.usermicroservice.server.dto.AdminAuthorityDto;
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
