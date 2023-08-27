/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuantran.repository.impl;

import com.tuantran.pojo.Fooditems;
import com.tuantran.repository.FoodItemsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Transactional
@Repository
@PropertySource("classpath:configs.properties")
public class FoodItemsRepositoryImpl implements FoodItemsRepository{

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;
    
    @Override
    public List<Object[]> getFoodItems(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
//        Query query = session.createQuery("From Fooditems");
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Fooditems> q = b.createQuery(Fooditems.class);
        Root root = q.from(Fooditems.class);
        q.select(root);
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("foodName"), String.format("%%%s%%", kw)));
            }
            
            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"), Double.parseDouble(toPrice)));
            }
            
            String cateFoodId = params.get("cateFoodId");
            if (cateFoodId != null && !cateFoodId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryfoodId"), Integer.valueOf(cateFoodId)));
            }
            
            String restaurantId = params.get("restaurantId");
            if (restaurantId != null && !restaurantId.isEmpty()) {
                predicates.add(b.equal(root.get("restaurantId"), Integer.valueOf(restaurantId)));
            }
            
            q.where(predicates.toArray(Predicate[]::new));
            
        }
          
        q.orderBy(b.desc(root.get("foodId")));
        Query query = session.createQuery(q);
        
        if (params != null) {
            String page = params.get("page");
            if (page != null && !page.isEmpty()) {
                int p = Integer.parseInt(page);
                int pageSize = Integer.parseInt(this.env.getProperty("PAGE_SIZE"));

                query.setMaxResults(pageSize);
                query.setFirstResult((p - 1) * pageSize);
            }
        }
        
        return query.getResultList();
    }

    @Override
    public boolean addOrUpdateFoodItem(Fooditems foodItem) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            if (foodItem.getFoodId()== null) {
                session.save(foodItem);
            } else {
                session.update(foodItem);
            }

            return true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Fooditems getFoodItemById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Fooditems.class, id);
    }
    
    @Override
    public boolean delFoodItem(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Fooditems food = this.getFoodItemById(id);
        try{
            session.delete(food);
            return true;
        }catch(HibernateException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Fooditems> getAllFoodItem() {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM Fooditems");
        return query.getResultList();
    }
    
}
