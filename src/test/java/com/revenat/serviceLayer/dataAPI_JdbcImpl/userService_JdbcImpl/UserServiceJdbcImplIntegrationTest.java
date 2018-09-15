package com.revenat.serviceLayer.dataAPI_JdbcImpl.userService_JdbcImpl;

import com.revenat.serviceLayer.UserServiceAbstractTestCase;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManagerImpl.MySQLSimpleConnectionManager;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl.DaoFactoryJdbcImpl;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executorsImpl.ExecutorImpl;

public class UserServiceJdbcImplIntegrationTest extends UserServiceAbstractTestCase {

    @Override
    public void setUp() throws Exception {
        String databaseName = "otus_hibernate_service";
        String login = "root";
        String password = "19900225";
        userService = new UserServiceJdbcImpl(
                new MySQLSimpleConnectionManager(databaseName, login, password),
                new DaoFactoryJdbcImpl(),
                new ExecutorImpl());
    }
}