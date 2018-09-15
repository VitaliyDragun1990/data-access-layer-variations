package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI.addressDao.AddressDao;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddressJdbcDao extends AbstractDao<Address, Long> implements AddressDao {
    private final static Logger logger = LoggerFactory.getLogger(AddressJdbcDao.class);

    private static String SELECT_BY_USER_ID = "SELECT * FROM address WHERE user_id = %d";
    private static String SELECT_BY_ID = "SELECT * FROM address WHERE address_id = %d";
    private static String SELECT_ALL = "SELECT * FROM address";
    private static String INSERT_ADDRESS = "INSERT INTO address (city, street, user_id) VALUES ('%s', '%s', %d)";
    private static String UPDATE_ADDRESS = "UPDATE address SET city = '%s', street = '%s' WHERE address_id = %d";
    private static String DELETE_ADDRESS = "DELETE FROM address WHERE address_id = %d";

    private Executor queryExecutor;

    public AddressJdbcDao(Executor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @Override
    public Optional<Address> findByUserId(Long userId) {
        return findBy(userId, SELECT_BY_USER_ID);
    }

    @Override
    public Optional<Address> findById(Long id) {
       return findBy(id, SELECT_BY_ID);
    }

    private Optional<Address> findBy(Long identifier, String queryTemplate) {
        Objects.requireNonNull(identifier, "Provided id must not be null");

        String query = String.format(queryTemplate, identifier);
        List<Address> addresses = queryExecutor.executeQuery(query, this::createAddressEntitiesFrom);

        Address address = null;
        if (addresses.size() > 0) {
            address = addresses.get(0);
        }

        return Optional.ofNullable(address);
    }

    @Override
    public List<Address> findAll() {
        return queryExecutor.executeQuery(SELECT_ALL, this::createAddressEntitiesFrom);
    }

    @Override
    public void save(Address entity, Long userId) {
        if (entity == null) {
            return;
        }

        Objects.requireNonNull(userId, "provided id must not be null");
        if (entity.getId() != null) {
            throw new RuntimeException("Provided address entity must be transient without id");
        }

        String query = String.format(INSERT_ADDRESS, entity.getCity(), entity.getStreet(), userId);
        queryExecutor.executeQuery(query);

        Long addressId = queryExecutor.executeQuery(ID_QUERY, this::retrieveEntityId);
        entity.setId(addressId);
    }

    @Override
    public Address update(Address entity) {
        if (entity == null) {
            return null;
        }

        Objects.requireNonNull(entity.getId(), "update() can be applied only to previously persisted entity with id");

        String query = String.format(UPDATE_ADDRESS, entity.getCity(), entity.getStreet(), entity.getId());
        queryExecutor.executeQuery(query);

        return entity;
    }

    @Override
    public void update(Address address, Long userId) {
        Objects.requireNonNull(userId, "You must provide userId to update appropriate address");

        if (address != null) {
            update(address);
        }

        Optional<Address> userAddress = findByUserId(userId);
        userAddress.ifPresent(this::delete);
    }

    @Override
    public void delete(Address entity) {
        if (entity == null) {
            return;
        }

        Objects.requireNonNull(entity.getId(),  "delete() can be applied only to previously persisted entity with id");

        String query = String.format(DELETE_ADDRESS, entity.getId());
        queryExecutor.executeQuery(query);
    }

    @Override
    public void save(Address entity) {
        throw new UnsupportedOperationException("Address entity can't be saved without user_id foreign key");
    }

    List<Address> createAddressEntitiesFrom(ResultSet resultSet) {
        List<Address> addresses = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Address address = new Address();
                address.setId(resultSet.getLong("address_id"));
                address.setCity(resultSet.getString("city"));
                address.setStreet(resultSet.getString("street"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return addresses;
    }
}
