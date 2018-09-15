package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.PhoneNumber;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PhoneNumberJdbcDaoTest {
    private final static Long PHONE_ID = 10L;
    private final static Long USER_ID = 20L;
    private final static String NUMBER = "555";

    private final static String SELECT_BY_USER_ID = "SELECT * FROM phone WHERE user_id = " + USER_ID;
    private final static String SELECT_BY_ID = "SELECT * FROM phone WHERE phone_id = " + PHONE_ID;
    private final static String SELECT_ALL = "SELECT * FROM phone";
    private final static String INSERT_PHONE = "INSERT INTO phone (phone_number, user_id) " +
            "VALUES ('" +
            NUMBER +
            "', " +
            USER_ID +
            ")";
    private final static String UPDATE_PHONE = "UPDATE phone " +
            "SET " +
            "phone_number = '" + NUMBER + "' " +
            "WHERE phone_id = " + PHONE_ID;
    private final static String DELETE_PHONE = "DELETE FROM phone WHERE phone_id = " + PHONE_ID;
    private final static String ID_QUERY = "SELECT @@IDENTITY AS IDENTITY";

    @Mock
    private Executor mockExecutor;
    @Mock
    private ResultSet mockResultSet;

    private PhoneNumber phoneNumber;

    private PhoneNumberJdbcDao dao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dao = new PhoneNumberJdbcDao(mockExecutor);

        phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(NUMBER);
    }

    @Test
    public void findByUserIdCallsExecutorWithCorrectQuery() {
        when(mockExecutor.executeQuery(any(String.class), any())).thenReturn(Collections.emptyList());

        dao.findByUserId(USER_ID);

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_BY_USER_ID), any());
    }

    @Test
    public void findByIdCallsExecutorWithCorrectQuery() {
        when(mockExecutor.executeQuery(any(String.class), any())).thenReturn(Collections.emptyList());

        dao.findById(PHONE_ID);

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_BY_ID), any());
    }

    @Test
    public void findAllCallsExecutorWithCorrectQuery() {
        dao.findAll();

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_ALL), any());
    }

    @Test
    public void saveCallsExecutorWithCorrectQueries() {
        dao.save(phoneNumber, USER_ID);

        verify(mockExecutor, times(1)).executeQuery(INSERT_PHONE);
        verify(mockExecutor, times(1)).executeQuery(eq(ID_QUERY), any());
    }

    @Test
    public void saveProvidesIdToPhoneNumberEntity() {
        assertNull(phoneNumber.getId());
        when(mockExecutor.executeQuery(eq(ID_QUERY), any())).thenReturn(PHONE_ID);

        dao.save(phoneNumber, USER_ID);

        assertThat(phoneNumber.getId(), equalTo(PHONE_ID));
    }

    @Test(expected = RuntimeException.class)
    public void saveThrowsExceptionIfProvidedEntityHasId() {
        phoneNumber.setId(PHONE_ID);

        dao.save(phoneNumber, USER_ID);
    }

    @Test
    public void updateCallsExecutorWithCorrectQuery() {
        phoneNumber.setId(PHONE_ID);

        dao.update(phoneNumber);

        verify(mockExecutor, times(1)).executeQuery(eq(UPDATE_PHONE));
    }

    @Test(expected = RuntimeException.class)
    public void updateThrowsExceptionIfProvidedEntityDoesNotHaveId() {
        dao.update(phoneNumber);
    }

    @Test
    public void deleteCallsExecutorWithCorrectQuery() {
        phoneNumber.setId(PHONE_ID);

        dao.delete(phoneNumber);

        verify(mockExecutor, times(1)).executeQuery(eq(DELETE_PHONE));
    }

    @Test(expected = RuntimeException.class)
    public void deleteThrowsExceptionIfProvidedEntityDoesNotHaveId() {
        dao.delete(phoneNumber);
    }

    @Test
    public void populatesPhoneNumberEntitiesFromResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong(eq("phone_id"))).thenReturn(PHONE_ID);
        when(mockResultSet.getString("phone_number")).thenReturn(NUMBER);

        List<PhoneNumber> numbers = dao.createPhoneNumberEntitiesFrom(mockResultSet);

        assertThat(numbers.size(), equalTo(1));
        assertEquals(numbers.get(0), phoneNumber);
    }
}