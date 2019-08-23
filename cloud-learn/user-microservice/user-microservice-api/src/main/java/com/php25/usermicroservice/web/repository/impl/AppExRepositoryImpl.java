package com.php25.usermicroservice.web.repository.impl;

import com.php25.common.db.Db;
import com.php25.common.db.repository.JdbcDbRepositoryImpl;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.repository.AppExRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: penghuiping
 * @date: 2019/8/15 09:45
 * @description:
 */
public class AppExRepositoryImpl extends JdbcDbRepositoryImpl<App, String> implements AppExRepository {

    @Autowired
    private Db db;

    @Override
    public boolean insert(App app) {

        int rows = db.cndJdbc(App.class).insert(app);
        if(rows>0) {
            return true;
        }else {
            return false;
        }
    }
}
