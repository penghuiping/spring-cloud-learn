package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.BaseDbRepository;
import com.php25.usermicroservice.web.model.App;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:33
 * @description:
 */
public interface AppRepository extends BaseDbRepository<App, String> {

    boolean insert(App app);
}
