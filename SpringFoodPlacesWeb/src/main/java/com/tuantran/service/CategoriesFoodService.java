/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tuantran.service;

import com.tuantran.pojo.CategoriesFood;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public interface CategoriesFoodService {
    
    List<Object[]> getCategoriesFood(Map<String, String> params);
    int countCategoriesFood();
    boolean addOrUpdateCate(CategoriesFood cate);
    CategoriesFood getCategoryById(int id);
    boolean delCategory(int id);
}

