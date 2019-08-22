package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.Group;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author: penghuiping
 * @date: 2019/8/12 09:57
 * @description:
 */
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
}
