package com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoHibernateImpl;

import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoFactory.UserDaoFactory;
import org.hibernate.Session;

public class UserDaoFactoryImpl implements UserDaoFactory {
    @Override
    public UserDao createUserDao(Session session) {
        return new UserHibernateDao(session);
    }
}
