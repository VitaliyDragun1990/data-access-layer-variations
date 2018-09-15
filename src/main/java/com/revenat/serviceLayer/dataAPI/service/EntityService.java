package com.revenat.serviceLayer.dataAPI.service;

import java.util.List;
import java.util.Optional;

public interface EntityService<T, ID> {
    void save(T entity);

    T update(T entity);

    Optional<T> read(ID entityId);

    List<T> readAll();

    void delete(T entity);

    void shutdown();
}
