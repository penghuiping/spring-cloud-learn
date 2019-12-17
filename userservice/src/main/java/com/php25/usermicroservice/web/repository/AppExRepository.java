package com.php25.usermicroservice.web.repository;

import com.php25.common.db.repository.JdbcDbRepository;
import com.php25.usermicroservice.web.model.App;

/**
 * @author: penghuiping
 * @date: 2019/8/15 09:44
 * @description:
 */
public interface AppExRepository extends JdbcDbRepository<App, String> {


    boolean insert(App app);



}
