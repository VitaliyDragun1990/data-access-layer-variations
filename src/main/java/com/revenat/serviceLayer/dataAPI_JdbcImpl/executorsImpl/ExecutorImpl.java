package com.revenat.serviceLayer.dataAPI_JdbcImpl.executorsImpl;

import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.resultHandlers.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecutorImpl implements Executor {
    private final static Logger logger = LoggerFactory.getLogger(ExecutorImpl.class);

    private Connection connection;

    @Override
    public void executeQuery(String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T executeQuery(String query, ResultHandler<T> handler) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
