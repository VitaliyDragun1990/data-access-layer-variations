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

    private final String databaseUrl;
    private final String databaseName;
    private final String login;
    private final String password;

    public MySQLSimpleConnectionManager(ConnectionProperties connectionProperties) {
        this.databaseUrl = connectionProperties.getDatabaseUrl();
        this.databaseName = connectionProperties.getDatabaseName();
        this.login = connectionProperties.getLogin();
        this.password = connectionProperties.getPassword();
    }

    @Override
    public Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());

            String url = String.format(databaseUrl, databaseName, login, password);

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
