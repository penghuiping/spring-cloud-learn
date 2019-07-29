package com.php25.usermicroservice.server.repository;

import com.php25.usermicroservice.server.model.Oauth2Client;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:33
 * @description:
 */
@Repository
public interface Oauth2ClientRepository extends PagingAndSortingRepository<Oauth2Client,String> {
}
