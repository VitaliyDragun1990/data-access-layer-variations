package com.revenat.serviceLayer.dataAPI_JdbcImpl.cacheServiceImpl;

import com.revenat.serviceLayer.dataAPI.cacheService.CacheService;
import com.revenat.serviceLayer.entities.User;
import com.revenat.simple_cache.cache.CacheElement;
import com.revenat.simple_cache.cache.CacheEngine;

import java.util.Optional;

public class UserCacheService implements CacheService<Long, User> {
    private CacheEngine<Long, User> cache;

    public UserCacheService(CacheEngine<Long, User> cache) {
        this.cache = cache;
    }

    @Override
    public void addOrUpdate(User user) {
        cache.put(new CacheElement<>(user.getId(), user));
    }


    @Override
    public Optional<User> get(Long id) {
        CacheElement<Long, User> elementRef = cache.get(id);
        User user = elementRef != null ? elementRef.getValue() : null;
        return Optional.ofNullable(user);
    }


    @Override
    public void delete(User user) {
        cache.delete(user.getId());
    }

    @Override
    public void shutdown() {
        cache.deleteAll();
        cache.dispose();
    }
}
