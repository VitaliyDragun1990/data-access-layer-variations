package com.revenat.serviceLayer.dataAPI_HibernateImpl.userServiceHibernateImpl;

import com.revenat.serviceLayer.entities.User;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProvider.SessionFactoryProvider;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoFactory.UserDaoFactory;
import com.revenat.serviceLayer.dataAPI.userService.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class UserServiceHibernateImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceHibernateImpl.class);

    private SessionFactory sessionFactory;
    private SessionFactoryProvider sessionFactoryProvider;
    private UserDaoFactory daoFactory;

    public UserServiceHibernateImpl(SessionFactoryProvider sessionFactoryProvider, UserDaoFactory daoFactory) {
        this.sessionFactoryProvider = sessionFactoryProvider;
        this.daoFactory = daoFactory;
        this.sessionFactory = sessionFactoryProvider.getSessionFactory();
    }

    @Override
    public void save(User entity) {
        processInSession(session -> daoFactory.createUserDao(session).save(entity));
    }

    @Override
    public User update(User entity) {
        return runInSession(session -> daoFactory.createUserDao(session).update(entity));
    }

    @Override
    public Optional<User> read(Long id) {
        return runInSession(session -> daoFactory.createUserDao(session).findById(id));
    }

    @Override
    public List<User> readByFirstName(String firstName) {
        return runInSession(session -> daoFactory.createUserDao(session).findByFirstName(firstName));
    }

    @Override
    public List<User> readAll() {
        return runInSession(session -> daoFactory.createUserDao(session).findAll());
    }

    @Override
    public void delete(User entity) {
        processInSession(session -> daoFactory.createUserDao(session).delete(entity));
    }

    @Override
    public void shutdown() {
        this.sessionFactoryProvider.closeSessionFactory(sessionFactory);
    }

    private <R> R runInSession(Function<Session, R> sessionHandler) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = sessionHandler.apply(session);
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void processInSession(Consumer<Session> sessionHandler) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            sessionHandler.accept(session);
            transaction.commit();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
