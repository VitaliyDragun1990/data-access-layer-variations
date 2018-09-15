package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI.phoneNumberDao.PhoneNumberDao;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PhoneNumberJdbcDao extends AbstractDao<PhoneNumber, Long> implements PhoneNumberDao {
    private final static Logger logger = LoggerFactory.getLogger(PhoneNumberJdbcDao.class);

    private static String SELECT_BY_USER_ID = "SELECT * FROM phone WHERE user_id = %d";
    private static String SELECT_BY_ID = "SELECT * FROM phone WHERE phone_id = %d";
    private static String SELECT_ALL = "SELECT * FROM phone";
    private static String INSERT_PHONE = "INSERT INTO phone (phone_number, user_id) VALUES ('%s', %d)";
    private static String UPDATE_PHONE = "UPDATE phone SET phone_number = '%s' WHERE phone_id = %d";
    private static String DELETE_PHONE = "DELETE FROM phone WHERE phone_id = %d";

    private Executor queryExecutor;

    public PhoneNumberJdbcDao(Executor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @Override
    public List<PhoneNumber> findByUserId(Long userId) {
        Objects.requireNonNull(userId, "Provided id must not be null");

        String query = String.format(SELECT_BY_USER_ID, userId);
        return queryExecutor.executeQuery(query, this::createPhoneNumberEntitiesFrom);
    }

    @Override
    public Optional<PhoneNumber> findById(Long id) {
        Objects.requireNonNull(id, "Provided id must not be null");

        String query = String.format(SELECT_BY_ID, id);
        List<PhoneNumber> numbers = queryExecutor.executeQuery(query, this::createPhoneNumberEntitiesFrom);

        PhoneNumber number = null;
        if (numbers.size() > 0) {
            number = numbers.get(0);
        }

        return Optional.ofNullable(number);
    }

    @Override
    public List<PhoneNumber> findAll() {
        return queryExecutor.executeQuery(SELECT_ALL, this::createPhoneNumberEntitiesFrom);
    }

    @Override
    public void save(PhoneNumber entity, Long userId) {
        if (entity == null) {
            return;
        }

        Objects.requireNonNull(userId, "Provided id must not be null");
        if (entity.getId() != null) {
            throw new RuntimeException("Provided phone entity must be transient without id");
        }

        String query = String.format(INSERT_PHONE, entity.getNumber(), userId);
        queryExecutor.executeQuery(query);

        Long phoneId = queryExecutor.executeQuery(ID_QUERY, this::retrieveEntityId);
        entity.setId(phoneId);
    }

    @Override
    public PhoneNumber update(PhoneNumber entity) {
        if (entity == null) {
            return null;
        }

        updateEntity(entity);

        return entity;
    }

    private void updateEntity(PhoneNumber entity) {
        Objects.requireNonNull(entity.getId(), "update() can be applied only to previously persisted entity with id");

        String query = String.format(UPDATE_PHONE, entity.getNumber(), entity.getId());
        queryExecutor.executeQuery(query);
    }

    @Override
    public void update(List<PhoneNumber> phones, Long userId) {
        Objects.requireNonNull(phones);
        Objects.requireNonNull(userId, "You must provide user_id to update appropriate phone numbers");

        deleteIfNotIn(phones, userId);

        phones.forEach(this::updateEntity);
    }

    private void deleteIfNotIn(List<PhoneNumber> userPhones, Long userId) {
        List<String> numbers = userPhones.stream().map(PhoneNumber::getNumber).collect(Collectors.toList());

        findByUserId(userId).stream()
                .filter(phoneNumber -> !numbers.contains(phoneNumber.getNumber()))
                .forEach(this::delete);
    }

    @Override
    public void delete(PhoneNumber entity) {
        if (entity == null) {
            return;
        }

        Objects.requireNonNull(entity.getId(), "delete() can be applied only to previously persisted entity with id");

        String query = String.format(DELETE_PHONE, entity.getId());
        queryExecutor.executeQuery(query);
    }

    @Override
    public void save(PhoneNumber entity) {
        throw new UnsupportedOperationException("PhoneNumber entity can't be saved without user_id foreign key");
    }

    List<PhoneNumber> createPhoneNumberEntitiesFrom(ResultSet resultSet) {
        List<PhoneNumber> numbers = new ArrayList<>();

        try {
            while (resultSet.next()) {
                PhoneNumber number = new PhoneNumber();
                number.setId(resultSet.getLong("phone_id"));
                number.setNumber(resultSet.getString("phone_number"));
                numbers.add(number);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return numbers;
    }
}
