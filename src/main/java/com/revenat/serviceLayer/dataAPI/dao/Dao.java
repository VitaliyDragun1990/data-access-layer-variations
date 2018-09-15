package com.revenat.serviceLayer.dataAPI.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {

    Optional<T> findById(ID id);

    List<T> findAll();

    void save(T entity);

    T update(T entity);

    void delete(T entity);
}
