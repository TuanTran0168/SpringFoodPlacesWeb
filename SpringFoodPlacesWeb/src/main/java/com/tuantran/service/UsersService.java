/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tuantran.service;

import com.tuantran.pojo.Users;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrator
 */
@Service
public interface UsersService {

    List<Object[]> getUsers(Map<String, String> params);
    int countUsers();
     boolean addOrUpdateUsers(Users user);
    Users getUserById(int id);
}
