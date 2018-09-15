package com.revenat.serviceLayer.dataAPI.userService;

import com.revenat.serviceLayer.entities.User;
import com.revenat.serviceLayer.dataAPI.service.EntityService;

import java.util.List;

public interface UserService extends EntityService<User, Long> {
    List<User> readByFirstName(String firstName);
}
