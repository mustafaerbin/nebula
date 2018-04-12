package com.tr.nebula.cache.config;

/**
 * Created by Ali Kızılırmak on 22.05.2017.
 */
public interface ICacheService {
    /**
     *
     * @param key
     * @return value
     */
    Object get(Object key);

    /**
     *
     * @param key
     * @param value
     */
    void put(Object key, Object value);
}


