package com.revenat.serviceLayer.dataAPI_JdbcImpl.resultHandlers;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultHandler<T> {
    T handle(ResultSet resultSet);
}
