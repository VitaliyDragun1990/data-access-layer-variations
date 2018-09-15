package com.revenat.serviceLayer.dataAPI_JdbcImpl.executors;

import com.revenat.serviceLayer.dataAPI_JdbcImpl.resultHandlers.ResultHandler;

import java.sql.Connection;

public interface Executor {
    void executeQuery(String query);

    <T> T executeQuery(String query, ResultHandler<T> handler);

    void setConnection(Connection connection);
}
