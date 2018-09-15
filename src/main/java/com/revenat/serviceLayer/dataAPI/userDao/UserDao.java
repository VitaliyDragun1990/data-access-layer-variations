package com.revenat.serviceLayer.dataAPI.userDao;

import com.revenat.serviceLayer.dataAPI.dao.Dao;
import com.revenat.serviceLayer.entities.User;

import java.util.List;

public interface UserDao extends Dao<User, Long> {
    List<User> findByFirstName(String firstName);
}
