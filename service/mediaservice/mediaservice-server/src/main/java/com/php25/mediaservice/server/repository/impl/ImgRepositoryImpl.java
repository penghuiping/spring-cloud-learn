package com.php25.mediaservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.mediaservice.server.model.Img;
import com.php25.mediaservice.server.repository.ImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: penghuiping
 * @date: 2019/1/4 11:11
 * @description:
 */
public class ImgRepositoryImpl extends BaseRepositoryImpl<Img, String> implements ImgRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Db db;


}
