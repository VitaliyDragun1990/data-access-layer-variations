package com.revenat.serviceLayer.dataAPI_JdbcImpl.userService_JdbcImpl;

import com.revenat.serviceLayer.UserServiceAbstractTestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceJdbcImplIntegrationTest extends UserServiceAbstractTestCase {

    @Override
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationJdbcContext.xml");
        userService = context.getBean("userServiceJdbc", UserServiceJdbcImpl.class);
    }
}