package com.revenat.serviceLayer.dataAPI_HibernateImpl.userServiceHibernateImpl;

import com.revenat.serviceLayer.UserServiceAbstractTestCase;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProviderImpl.SessionFactoryProviderImpl;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoHibernateImpl.UserDaoFactoryImpl;

public class UserServiceHibernateImplIntegrationTest extends UserServiceAbstractTestCase {

    @Override
    public void setUp() throws Exception {
        userService = new UserServiceHibernateImpl(new SessionFactoryProviderImpl(), new UserDaoFactoryImpl());
    }
}