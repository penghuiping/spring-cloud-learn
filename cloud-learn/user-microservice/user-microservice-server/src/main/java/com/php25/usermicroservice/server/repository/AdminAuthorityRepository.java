package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.AdminAuthority;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author: penghuiping
 * @date: 2018/10/17 13:59
 * @description:
 */
public interface AdminAuthorityRepository extends PagingAndSortingRepository<AdminAuthority, Long> {


}
