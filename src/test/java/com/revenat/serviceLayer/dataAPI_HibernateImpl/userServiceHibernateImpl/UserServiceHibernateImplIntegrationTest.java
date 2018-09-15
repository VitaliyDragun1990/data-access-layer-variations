package com.revenat.serviceLayer.dataAPI_HibernateImpl.userServiceHibernateImpl;

import com.revenat.serviceLayer.UserServiceAbstractTestCase;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProviderImpl.SessionFactoryProviderImpl;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoHibernateImpl.UserDaoFactoryImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceHibernateImplIntegrationTest extends UserServiceAbstractTestCase {

    @Override
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationHibernateContext.xml");
        userService = context.getBean("userServiceHibernate", UserServiceHibernateImpl.class);
    }
}