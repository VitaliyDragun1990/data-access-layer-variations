package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI.dao.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract class AbstractDao<T, ID> implements Dao<T, ID> {
    static String ID_QUERY = "SELECT @@IDENTITY AS IDENTITY";

    protected Long retrieveEntityId(ResultSet resultSet) {
        try {
            resultSet.next();
            return resultSet.getLong("IDENTITY");
        } catch (SQLException e) {
            throw new RuntimeException("Can't retrieve last inserted entity's id from database");
        }
    }
}
