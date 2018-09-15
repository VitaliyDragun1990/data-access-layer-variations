package com.revenat.serviceLayer;

import com.revenat.serviceLayer.dataAPI.userService.UserService;
import com.revenat.serviceLayer.entities.Address;
import com.revenat.serviceLayer.entities.PhoneNumber;
import com.revenat.serviceLayer.entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public abstract class UserServiceAbstractTestCase {
    private static final Long DUMMY_ID = 5555L;
    private static final PhoneNumber PHONE_A = new PhoneNumber("111");
    private static final PhoneNumber PHONE_B = new PhoneNumber("222");

    private static final LocalDate BIRTH_DATE = LocalDate.of(1985, 12, 12);
    private static final String FIRST_NAME = "Jack";
    private static final String LAST_NAME = "Smith";


    protected UserService userService;

    @Before
    public abstract void setUp() throws Exception;

    @After
    public void tearDown() {
        deleteAllUsers();
    }

    private void deleteAllUsers() {
        List<User> users = userService.readAll();
        for (User user : users)
            userService.delete(user);
    }

    @Test
    public void returnsEmptyOptionalIfNoUserWithId() {
        Optional<User> optionalUser = userService.read(DUMMY_ID);

        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void returnsEmptyListIfNoUsersInDatabase() {
        List<User> users = userService.readAll();

        assertThat(users.size(), equalTo(0));
    }

    @Test
    public void savingUserGeneratesId() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        assertNull(user.getId());

        userService.save(user);
        assertNotNull(user.getId());
    }

    @Test
    public void readsSavedUserFromDatabase() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        userService.save(user);

        Optional<User> read = userService.read(user.getId());
        User loadedUser = read.get();

        assertEqualUsers(loadedUser, user);
    }

    @Test
    public void deletesSavedUserFromDatabase() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        userService.save(user);

        userService.delete(user);

        Optional<User> read = userService.read(user.getId());
        assertFalse(read.isPresent());
    }

    @Test
    public void readsAllUsersFromDatabase() {
        User jack = new User("Jack", "Smith", BIRTH_DATE);
        User anna = new User("Anna", "Smith", BIRTH_DATE);
        User lory = new User("Lory", "Smith", BIRTH_DATE);

        saveUsers(jack, anna, lory);

        List<User> loadUsers = userService.readAll();
        assertThat(loadUsers.size(), equalTo(3));
    }

    @Test
    public void savesUserWithAddress() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        Address address = new Address("NY", "Main Street");
        user.setAddress(address);

        userService.save(user);

        Optional<User> optionalUser = userService.read(user.getId());
        User retrieved = optionalUser.get();
        assertThat(retrieved.getAddress(), equalTo(address));
    }

    @Test
    public void savesUserWithPhones() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        user.addPhoneNumber(new PhoneNumber("111"));
        user.addPhoneNumber(new PhoneNumber("222"));

        userService.save(user);

        User loadUser = userService.read(user.getId()).get();
        List<PhoneNumber> phones = loadUser.getPhones();
        assertThat(phones.size(), equalTo(2));
        assertThat(phones, contains(PHONE_A, PHONE_B));
    }


    @Test
    public void deletesAddressIfUpdateUserWithoutAddress() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        user.setAddress(new Address("NY", "Main street"));
        userService.save(user);

        user.setAddress(null);
        userService.update(user);

        User updatedUser = userService.read(user.getId()).get();
        assertThat(updatedUser.getAddress(), equalTo(null));
    }

    @Test
    public void deletesPhoneIfUpdateUserWithoutPhone() {
        User user = new User(FIRST_NAME, LAST_NAME, BIRTH_DATE);
        PhoneNumber phoneA = new PhoneNumber("555");
        PhoneNumber phoneB = new PhoneNumber("666");
        user.addPhoneNumber(phoneA);
        user.addPhoneNumber(phoneB);
        userService.save(user);

        user.removePhoneNumber(phoneA);
        userService.update(user);


        User updatedUser = userService.read(user.getId()).get();
        List<PhoneNumber> phones = updatedUser.getPhones();
        assertThat(phones.size(), equalTo(1));
        assertThat(phones, contains(phoneB));
    }

    private void saveUsers(User... users) {
        for (User user : users) {
            userService.save(user);
        }
    }

    private void assertEqualUsers(User actual, User expected) {
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getFirstName(), equalTo(expected.getFirstName()));
        assertThat(actual.getLastName(), equalTo(expected.getLastName()));
        assertThat(actual.getBirthDate(), equalTo(expected.getBirthDate()));
    }
}
