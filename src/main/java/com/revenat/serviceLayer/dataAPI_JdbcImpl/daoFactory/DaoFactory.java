package com.revenat.serviceLayer.dataAPI_JdbcImpl.daoFactory;

import com.revenat.serviceLayer.dataAPI.addressDao.AddressDao;
import com.revenat.serviceLayer.dataAPI.phoneNumberDao.PhoneNumberDao;
import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import com.revenat.serviceLayer.dataAPI_JdbcImpl.executors.Executor;

public interface DaoFactory {
    UserDao createUserDao(Executor executor);

    AddressDao createAddressDao(Executor executor);

    PhoneNumberDao createPhoneNumberDao(Executor executor);
}
