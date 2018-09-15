package com.revenat.serviceLayer.dataAPI.phoneNumberDao;

import com.revenat.serviceLayer.dataAPI.dao.Dao;
import com.revenat.serviceLayer.entities.PhoneNumber;

import java.util.List;

public interface PhoneNumberDao extends Dao<PhoneNumber, Long> {
    List<PhoneNumber> findByUserId(Long userId);
    void save(PhoneNumber phoneNumber, Long userId);
    void update(List<PhoneNumber> phoneNumbers, Long userId);
}
