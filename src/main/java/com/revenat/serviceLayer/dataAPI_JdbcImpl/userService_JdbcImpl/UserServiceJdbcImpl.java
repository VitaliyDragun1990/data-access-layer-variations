package com.revenat.serviceLayer.dataAPI_JdbcImpl.userService_JdbcImpl;

import com.revenat.serviceLayer.dataAPI.addressDao.AddressDao;
import com.revenat.serviceLayer.dataAPI.cacheService.CacheService;
import com.revenat.serviceLayer.dataAPI.phoneNumberDao.PhoneNumberDao;
import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import com.revenat.serviceLayer.dataAPI.userService.UserService;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.cacheServiceImpl.UserCacheService;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManager.ConnectionManager;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.daoFactory.DaoFactory;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.Address;
import com.revenat.serviceLayer.entities.PhoneNumber;
import com.revenat.serviceLayer.entities.User;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserServiceJdbcImpl implements UserService {
    private UserDao userDao;
    private AddressDao addressDao;
    private PhoneNumberDao phoneDao;

    private ConnectionManager connectionManager;
    private Connection connection;
    private CacheService<Long, User> cacheService;

    public UserServiceJdbcImpl(ConnectionManager connectionManager, DaoFactory daoFactory, Executor executor,
                               CacheService<Long, User> cacheService) {
        this.connection = connectionManager.getConnection();
        this.connectionManager = connectionManager;
        executor.setConnection(connection);
        this.userDao = daoFactory.createUserDao(executor);
        this.addressDao = daoFactory.createAddressDao(executor);
        this.phoneDao = daoFactory.createPhoneNumberDao(executor);
        this.cacheService = cacheService;
    }

    @Override
    public List<User> readByFirstName(String firstName) {
        List<User> users = userDao.findByFirstName(firstName);

        initializeUserEntities(users);

        addToCache(users);

        return users;
    }

    private void initializeUserEntities(List<User> users) {
        for (User user : users) {
            initializeUserEntity(user);
        }
    }

    private void initializeUserEntity(User user) {
        Optional<Address> address = addressDao.findByUserId(user.getId());
        List<PhoneNumber> phones = phoneDao.findByUserId(user.getId());
        address.ifPresent(user::setAddress);
        phones.forEach(user::addPhoneNumber);
    }

    private void addToCache(List<User> users) {
        for (User user : users) {
            cacheService.addOrUpdate(user);
        }
    }

    @Override
    public void save(User user) {
        userDao.save(user);
        addressDao.save(user.getAddress(), user.getId());
        user.getPhones().forEach(phone -> phoneDao.save(phone, user.getId()));

        cacheService.addOrUpdate(user);
    }

    @Override
    public User update(User user) {
        userDao.update(user);
        addressDao.update(user.getAddress(), user.getId());
        phoneDao.update(user.getPhones(), user.getId());

        cacheService.addOrUpdate(user);

        return user;
    }

    @Override
    public Optional<User> read(Long userId) {
        Optional<User> userOptional = cacheService.get(userId);

        if (isEmpty(userOptional)) {
            userOptional = userDao.findById(userId);
            userOptional.ifPresent(this::initializeUserEntity);

            userOptional.ifPresent(user -> cacheService.addOrUpdate(user));
        }

        return userOptional;
    }

    private boolean isEmpty(Optional<User> userOptional) {
        return !userOptional.isPresent();
    }

    @Override
    public List<User> readAll() {
        List<User> users = userDao.findAll();

        initializeUserEntities(users);

        addToCache(users);

        return users;
    }

    @Override
    public void delete(User user) {
        Objects.requireNonNull(user, "Provided user instance must not be null");

        addressDao.delete(user.getAddress());
        user.getPhones().forEach(phone -> phoneDao.delete(phone));
        userDao.delete(user);

        cacheService.delete(user);
    }

    @Override
    public void shutdown() {
        this.connectionManager.closeConnection(connection);
        cacheService.shutdown();
    }
}
