package com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoFactory;

import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import org.hibernate.Session;

public interface UserDaoFactory {
    UserDao createUserDao(Session session);
}
