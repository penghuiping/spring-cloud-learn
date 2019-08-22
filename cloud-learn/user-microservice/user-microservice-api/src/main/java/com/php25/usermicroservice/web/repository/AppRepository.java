package com.php25.usermicroservice.web.repository;

import com.php25.usermicroservice.web.model.App;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:33
 * @description:
 */
public interface AppRepository extends PagingAndSortingRepository<App, String>, AppExRepository {


}
