package com.php25.mediamicroservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.mediamicroservice.server.model.Img;
import com.php25.mediamicroservice.server.repository.ImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author: penghuiping
 * @date: 2019/1/4 11:11
 * @description:
 */
@Repository
public class ImgRepositoryImpl extends BaseRepositoryImpl<Img, String> implements ImgRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Db db;


}
