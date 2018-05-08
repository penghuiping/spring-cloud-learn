package com.php25.userservice.server.repository.impl;

import com.php25.userservice.server.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by penghuiping on 2/20/15.
 */
public class AdminUserRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public AdminUser findByLoginNameAndPassword(String loginName, String password) {
        Query query = entityManager.createQuery("select a from AdminUser a where a.username=?1 and a.password=?2 and a.enable=1");
        query.setParameter(1, loginName);
        query.setParameter(2, password);
        try {
            return (AdminUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
