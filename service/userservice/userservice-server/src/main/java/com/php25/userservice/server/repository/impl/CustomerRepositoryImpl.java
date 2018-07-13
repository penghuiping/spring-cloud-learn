package com.php25.userservice.server.repository.impl;

import com.php25.userservice.server.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by penghuiping on 2/20/15.
 */
public class CustomerRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Customer findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("select a from Customer a where a.username=?1 and a.password=?2 and a.enable=1");
        query.setParameter(1, username);
        query.setParameter(2, password);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneByPhoneAndPassword(String phone, String password) {
        Query query = entityManager.createQuery("select a from Customer a where a.mobile=?1 and a.password=?2 and a.enable=1");
        query.setParameter(1, phone);
        query.setParameter(2, password);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneByUidAndType(String uid, Integer type) {
        Query query = entityManager.createQuery("select a from Customer a where a.mobile=?1 and a.type=?2 and a.enable=1");
        query.setParameter(1, uid);
        query.setParameter(2, type);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneByPhone(String phone) {
        Query query = entityManager.createQuery("select a from Customer a where a.mobile=?1 and a.enable=1");
        query.setParameter(1, phone);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneByWx(String wx) {
        Query query = entityManager.createQuery("select a from Customer a where a.wx=?1 and a.enable=1");
        query.setParameter(1, wx);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneByQQ(String qq) {
        Query query = entityManager.createQuery("select a from Customer a where a.qq=?1 and a.enable=1");
        query.setParameter(1, qq);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer findOneBySina(String weibo) {
        Query query = entityManager.createQuery("select a from Customer a where a.weibo=?1 and a.enable=1");
        query.setParameter(1, weibo);
        List<Customer> rs = query.setFirstResult(0).setMaxResults(1).getResultList();
        if (rs.size() > 0) {
            Customer customer = rs.get(0);
            return customer;
        } else {
            return null;
        }
    }
}
