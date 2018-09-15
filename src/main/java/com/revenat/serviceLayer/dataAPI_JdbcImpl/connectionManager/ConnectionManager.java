package com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManager;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();

    void closeConnection(Connection connection);
}
