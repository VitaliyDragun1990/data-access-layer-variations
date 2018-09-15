package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UserJdbcDaoTest {
    private static final long ID = 1L;
    private final static String FIRST_NAME = "Jack";
    private final static String LAST_NAME = "Smith";
    private final static LocalDate BIRTH_DATE = LocalDate.of(1988, 7, 25);

    private static final String SELECT_BY_FIRST_NAME = "SELECT * FROM user WHERE first_name = '" + FIRST_NAME + "'";
    private static final String SELECT_BY_ID = "SELECT * FROM user WHERE user_id = " + ID;
    private static final String SELECT_ALL = "SELECT * FROM user";
    private static final String INSERT_QUERY =
            "INSERT INTO user (first_name, last_name, birth_date) "
                    + "VALUES ('"
                    + FIRST_NAME
                    + "', '"
                    + LAST_NAME
                    + "', '"
                    + BIRTH_DATE.toString()
                    + "')";
    private static final String ID_QUERY = "SELECT @@IDENTITY AS IDENTITY";
    private static final String UPDATE_QUERY = "UPDATE user " +
            "SET " +
            "first_name = '" + FIRST_NAME + "', " +
            "last_name = '" + LAST_NAME + "', " +
            "birth_date = '" + BIRTH_DATE.toString() + "' " +
            "WHERE user_id = " + ID;
    private static final String DELETE_QUERY = "DELETE FROM user WHERE user_id = " + ID;


    @Mock
    private Executor mockExecutor;
    @Mock
    private ResultSet mockResultSet;

    private User user;

    private UserJdbcDao userDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userDao = new UserJdbcDao(mockExecutor);

        user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(BIRTH_DATE);
    }

    @Test
    public void findByFirstNameCallsExecutorWithCorrectQuery() {
        userDao.findByFirstName(FIRST_NAME);

        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        verify(mockExecutor, times(1)).executeQuery(ac.capture(), any());
        String query = ac.getValue();
        assertThat(query, equalTo(SELECT_BY_FIRST_NAME));
    }

    @Test
    public void findByIdCallsExecutorWithCorrectQuery() {
        when(mockExecutor.executeQuery(any(String.class), any())).thenReturn(Collections.emptyList());
        userDao.findById(ID);

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_BY_ID), any());
    }

    @Test
    public void findAllCallsExecutorWithCorrectQuery() {
        userDao.findAll();

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_ALL), any());
    }

    @Test
    public void saveCallsExecutorWithCorrectQueries() {
        userDao.save(user);

        verify(mockExecutor, times(1)).executeQuery(eq(INSERT_QUERY));
        verify(mockExecutor, times(1)).executeQuery(eq(ID_QUERY), any());
    }

    @Test
    public void saveProvidesIdToUserEntity() {
        assertNull(user.getId());
        when(mockExecutor.executeQuery(eq(ID_QUERY), any())).thenReturn(ID);

        userDao.save(user);

        assertThat(user.getId(), equalTo(ID));
    }

    @Test(expected = RuntimeException.class)
    public void saveThrowsExceptionIfProvidedEntityWithId() {
        user.setId(ID);
        userDao.save(user);
    }

    @Test
    public void updateCallsExecutorWithCorrectQuery() {
        user.setId(ID);

        userDao.update(user);

        verify(mockExecutor, times(1)).executeQuery(UPDATE_QUERY);
    }

    @Test(expected = RuntimeException.class)
    public void updateThrowsExceptionIfProvidedEntityWithoutId() {
        userDao.update(user);
    }

    @Test
    public void deleteCallsExecutorWithCorrectQuery() {
        user.setId(ID);

        userDao.delete(user);

        verify(mockExecutor, times(1)).executeQuery(DELETE_QUERY);
    }

    @Test(expected = RuntimeException.class)
    public void deleteThrowsExceptionIfProvidedEntityWithoutId() {
        userDao.delete(user);
    }

    @Test
    public void populatesUserEntitiesFromResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong(eq("user_id"))).thenReturn(ID);
        when(mockResultSet.getString("first_name")).thenReturn(FIRST_NAME);
        when(mockResultSet.getString("last_name")).thenReturn(LAST_NAME);
        when(mockResultSet.getDate("birth_date")).thenReturn(Date.valueOf(BIRTH_DATE));

        List<User> users = userDao.createUserEntitiesFrom(mockResultSet);

        assertThat(users.size(), equalTo(1));
        assertEquals(users.get(0), user);
    }
}