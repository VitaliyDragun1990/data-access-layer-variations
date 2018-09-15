package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserJdbcDao extends AbstractDao<User, Long> implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserJdbcDao.class);

    private static String SELECT_BY_FIRST_NAME = "SELECT * FROM user WHERE first_name = '%s'";
    private static String SELECT_BY_ID = "SELECT * FROM user WHERE user_id = %d";
    private static String SELECT_ALL = "SELECT * FROM user";
    private static String INSERT_USER = "INSERT INTO user (first_name, last_name, birth_date) VALUES ('%s', '%s', '%s')";
    private static String UPDATE_USER = "UPDATE user SET first_name = '%s', last_name = '%s', birth_date = '%s' WHERE user_id = %d";
    private static String DELETE_USER = "DELETE FROM user WHERE user_id = %d";

    private Executor queryExecutor;

    public UserJdbcDao(Executor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        Objects.requireNonNull(firstName, "firstName must not be null");

        String query = String.format(SELECT_BY_FIRST_NAME, firstName);
        return queryExecutor.executeQuery(query, this::createUserEntitiesFrom);
    }

    @Override
    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        String query = String.format(SELECT_BY_ID, id);
        List<User> users = queryExecutor.executeQuery(query, this::createUserEntitiesFrom);

        User user = null;
        if (users.size() > 0) {
            user = users.get(0);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return queryExecutor.executeQuery(SELECT_ALL, this::createUserEntitiesFrom);
    }

    @Override
    public void save(User entity) {
        Objects.requireNonNull(entity, "Provided entity must not be null");
        if (entity.getId() != null) {
            throw new RuntimeException("Provided entity must be transient without id");
        }

        String query = String.format(INSERT_USER, entity.getFirstName(), entity.getLastName(), entity.getBirthDate());
        queryExecutor.executeQuery(query);

        Long userId = queryExecutor.executeQuery(ID_QUERY, this::retrieveEntityId);
        entity.setId(userId);
    }

    @Override
    public User update(User entity) {
        Objects.requireNonNull(entity, "Provided entity must not be null");
        Objects.requireNonNull(entity.getId(), "update() can be applied only to previously persisted entity with id");

        String query = String.format(UPDATE_USER, entity.getFirstName(), entity.getLastName(), entity.getBirthDate(), entity.getId());
        queryExecutor.executeQuery(query);

        return entity;
    }

    @Override
    public void delete(User entity) {
        Objects.requireNonNull(entity, "Provided entity must not be null");
        Objects.requireNonNull(entity.getId(), "delete() can be applied only to previously persisted entity with id");

        String query = String.format(DELETE_USER, entity.getId());
        queryExecutor.executeQuery(query);
    }

    List<User> createUserEntitiesFrom(ResultSet resultSet) {
        List<User> users = new ArrayList<>();

        try {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return users;
    }
}
