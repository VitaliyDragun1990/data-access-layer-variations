package com.revenat.serviceLayer.dataAPI.cacheService;

import java.util.Optional;

public interface CacheService<K, V> {

    void addOrUpdate(V entity);

    Optional<V> get(K key);

    void delete(V entity);

    void shutdown();
}
