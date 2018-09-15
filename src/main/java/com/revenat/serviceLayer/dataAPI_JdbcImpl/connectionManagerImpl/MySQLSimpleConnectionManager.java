package com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManagerImpl;

import com.mysql.jdbc.Driver;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManager.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLSimpleConnectionManager implements ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(MySQLSimpleConnectionManager.class);

    private static final String BASIC_URL = "jdbc:mysql://localhost:3306/%s?user=%s&password=%s&useSSL=false&allowMultiQueries=true";
    private final String databaseName;
    private final String login;
    private final String password;

    public MySQLSimpleConnectionManager(String databaseName, String login, String password) {
        this.databaseName = databaseName;
        this.login = login;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());

            String url = String.format(BASIC_URL, databaseName, login, password);

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            if (connection != null && isOpen(connection)) {
                connection.close();
            }
        } catch (SQLException e) {
            // ignore
        }
    }

    private boolean isOpen(Connection connection) throws SQLException {
        return !connection.isClosed();
    }
}
