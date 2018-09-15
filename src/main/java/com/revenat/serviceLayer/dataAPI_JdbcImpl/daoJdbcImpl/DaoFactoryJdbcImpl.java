package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoJdbcImpl;

import com.revenat.serviceLayer.dataAPI.addressDao.AddressDao;
import com.revenat.serviceLayer.dataAPI.phoneNumberDao.PhoneNumberDao;
import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.daoFactory.DaoFactory;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;

public class DaoFactoryJdbcImpl implements DaoFactory {
    @Override
    public UserDao createUserDao(Executor executor) {
        return new UserJdbcDao(executor);
    }

    @Override
    public AddressDao createAddressDao(Executor executor) {
        return new AddressJdbcDao(executor);
    }

    @Override
    public PhoneNumberDao createPhoneNumberDao(Executor executor) {
        return new PhoneNumberJdbcDao(executor);
    }
}
