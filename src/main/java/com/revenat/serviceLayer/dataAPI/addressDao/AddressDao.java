package com.revenat.serviceLayer.dataAPI.addressDao;

import com.revenat.serviceLayer.dataAPI.dao.Dao;
import com.revenat.serviceLayer.entities.Address;

import java.util.Optional;

public interface AddressDao extends Dao<Address, Long> {
    Optional<Address> findByUserId(Long userId);
    void save(Address address, Long userId);
    void update(Address address, Long userId);
}
