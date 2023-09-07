/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuantran.repository.impl;

import com.tuantran.pojo.ShelfLife;
import com.tuantran.repository.ShelfLifeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
@Transactional
public class ShelfLifeRepositoryImpl implements ShelfLifeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment environment;

    @Override
    public List<ShelfLife> getShelfLife(Map<String, String> params) {
//        Session session = this.factory.getObject().getCurrentSession();
//        Query query = session.createQuery("From ShelfLife");
//        return query.getResultList();
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ShelfLife> query = criteriaBuilder.createQuery(ShelfLife.class);
        Root rootShelfLife = query.from(ShelfLife.class);

        query.select(rootShelfLife);
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {

            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(rootShelfLife.get("shelflifeName"), String.format("%%%s%%", keyword))
                );
            }

            String restaurantId = params.get("restaurantId");
            if (restaurantId != null && !restaurantId.isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(rootShelfLife.get("restaurantId"), Integer.valueOf(restaurantId))
                );
            }

        }
        predicates.add(
                criteriaBuilder.equal(rootShelfLife.get("active"), Boolean.TRUE));
        query.where(predicates.toArray(Predicate[]::new));
        Query final_query = session.createQuery(query);

        if (params != null) {
            String pageStr = params.get("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                int pageInt = Integer.parseInt(pageStr);
                int pageSize = Integer.parseInt(this.environment.getProperty("PAGE_SIZE"));

//                final_query.setMaxResults(pageSize);
//                final_query.setFirstResult((pageInt - 1) * pageSize);
            }
        }
        return final_query.getResultList();
    }

    @Override
    public boolean addOrUpdateShelfLife(ShelfLife shelfLife) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            if (shelfLife.getShelflifeId() == null) {
                shelfLife.setActive(Boolean.TRUE);
                session.save(shelfLife);
            } else {
                session.update(shelfLife);
            }

            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ShelfLife getShelfLifeById(int id) {
//        Session session = this.factory.getObject().getCurrentSession();
//        return session.get(ShelfLife.class, id);
        try {
            Session session = this.factory.getObject().getCurrentSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ShelfLife> criteriaQuery = builder.createQuery(ShelfLife.class);
            Root<ShelfLife> root = criteriaQuery.from(ShelfLife.class);

            Predicate idPredicate = builder.equal(root.get("shelflifeId"), id);

            Predicate otherCondition = builder.equal(root.get("active"), Boolean.TRUE);

            Predicate finalPredicate = builder.and(idPredicate, otherCondition);

            criteriaQuery.where(finalPredicate);
            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delShelf(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ShelfLife shelfLife = this.getShelfLifeById(id);
        try {
            session.delete(shelfLife);
            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
