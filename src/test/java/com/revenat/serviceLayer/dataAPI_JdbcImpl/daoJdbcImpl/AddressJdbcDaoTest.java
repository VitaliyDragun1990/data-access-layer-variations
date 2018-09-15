package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;
import com.revenat.serviceLayer.entities.Address;
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

public class AddressJdbcDaoTest {
    private final static Long ADDRESS_ID = 1L;
    private final static Long USER_ID = 2L;
    private final static String CITY = "NY";
    private final static String STREET = "Main Street";

    private final static String SELECT_BY_USER_ID = "SELECT * FROM address WHERE user_id = " + USER_ID;
    private final static String SELECT_BY_ID = "SELECT * FROM address WHERE address_id = " + ADDRESS_ID;
    private final static String SELECT_ALL = "SELECT * FROM address";
    private final static String INSERT_ADDRESS = "INSERT INTO address (city, street, user_id) " +
            "VALUES ('" +
            CITY +
            "', '" +
            STREET +
            "', " +
            USER_ID +
            ")";
    private final static String UPDATE_ADDRESS = "UPDATE address " +
            "SET " +
            "city = '" + CITY + "', " +
            "street = '" + STREET + "' " +
            "WHERE address_id = " + ADDRESS_ID;
    private final static String DELETE_ADDRESS = "DELETE FROM address WHERE address_id = " + ADDRESS_ID;
    private final static String ID_QUERY = "SELECT @@IDENTITY AS IDENTITY";

    @Mock
    private Executor mockExecutor;
    @Mock
    private ResultSet mockResultSet;

    private Address address;

    private AddressJdbcDao addressDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addressDao = new AddressJdbcDao(mockExecutor);

        address = new Address();
        address.setCity(CITY);
        address.setStreet(STREET);
    }

    @Test
    public void findByUserIdCallsExecutorWithCorrectQuery() {
        when(mockExecutor.executeQuery(any(String.class), any())).thenReturn(Collections.emptyList());

        addressDao.findByUserId(USER_ID);

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_BY_USER_ID), any());
    }

    @Test
    public void findByIdCallsExecutorWithCorrectQuery() {
        when(mockExecutor.executeQuery(any(String.class), any())).thenReturn(Collections.emptyList());

        addressDao.findById(ADDRESS_ID);

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_BY_ID), any());
    }

    @Test
    public void findAllCallsExecutorWithCorrectQuery() {
        addressDao.findAll();

        verify(mockExecutor, times(1)).executeQuery(eq(SELECT_ALL), any());
    }

    @Test
    public void saveCallsExecutorWithCorrectQueries() {
        addressDao.save(address, USER_ID);

        verify(mockExecutor, times(1)).executeQuery(INSERT_ADDRESS);
        verify(mockExecutor, times(1)).executeQuery(eq(ID_QUERY), any());
    }

    @Test
    public void saveProvidesIdToAddressEntity() {
        assertNull(address.getId());
        when(mockExecutor.executeQuery(eq(ID_QUERY), any())).thenReturn(ADDRESS_ID);

        addressDao.save(address, USER_ID);

        assertThat(address.getId(), equalTo(ADDRESS_ID));
    }

    @Test(expected = RuntimeException.class)
    public void saveThrowsExceptionIfProvidedEntityHasId() {
        address.setId(ADDRESS_ID);
        addressDao.save(address, USER_ID);
    }

    @Test
    public void updateCallsExecutorWithCorrectQuery() {
        address.setId(ADDRESS_ID);

        addressDao.update(address);

        verify(mockExecutor, times(1)).executeQuery(UPDATE_ADDRESS);
    }

    @Test(expected = RuntimeException.class)
    public void updateThrowsExceptionIfProvidedEntityWithoutId() {
        addressDao.update(address);
    }

    @Test
    public void deleteCallsExecutorWithCorrectQuery() {
        address.setId(ADDRESS_ID);

        addressDao.delete(address);

        verify(mockExecutor, times(1)).executeQuery(DELETE_ADDRESS);
    }

    @Test(expected = RuntimeException.class)
    public void deleteThrowsExceptionIfProvidedEntityWithoutId() {
        addressDao.delete(address);
    }

    @Test
    public void populatesAddressEntitiesFromResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong(eq("address_id"))).thenReturn(ADDRESS_ID);
        when(mockResultSet.getString("city")).thenReturn(CITY);
        when(mockResultSet.getString("street")).thenReturn(STREET);

        List<Address> addresses = addressDao.createAddressEntitiesFrom(mockResultSet);

        assertThat(addresses.size(), equalTo(1));
        assertEquals(addresses.get(0), address);
    }
}