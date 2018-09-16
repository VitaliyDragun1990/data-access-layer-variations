package com.revenat.serviceLayer.dataAPI_JdbcImpl.userService_JdbcImpl;

import com.revenat.serviceLayer.UserServiceAbstractTestCase;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManager.ConnectionManager;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManagerImpl.MySQLSimpleConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserServiceJdbcImplIntegrationTest extends UserServiceAbstractTestCase {

    @Override
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationJdbcContext.xml");
        userService = context.getBean("userServiceJdbc", UserServiceJdbcImpl.class);
    }
}