package com.php25.usermicroservice.web.repository.impl;

import com.php25.common.db.repository.BaseDbRepositoryImpl;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.repository.AppRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2019/8/15 09:45
 * @description:
 */
@Repository
public class AppRepositoryImpl extends BaseDbRepositoryImpl<App, String> implements AppRepository {
    @Override
    public boolean insert(App app) {
        int rows = db.cndJdbc(App.class).insert(app);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }
}
