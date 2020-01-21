package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.BaseDbRepository;
import com.php25.usermicroservice.web.model.Group;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/8/12 09:57
 * @description:
 */
public interface GroupRepository extends BaseDbRepository<Group, Long> {

    Boolean softDelete(@Param("ids") List<Long> ids, @Param("appId") String appId);

    Boolean changeInfo(@Param("description") String description, @Param("id") Long id, @Param("appId") String appId);
}
