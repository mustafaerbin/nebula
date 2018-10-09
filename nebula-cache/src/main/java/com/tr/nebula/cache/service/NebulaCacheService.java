package com.tr.nebula.cache.service;

import com.tr.nebula.cache.config.ICacheService;
import com.tr.nebula.cache.config.NebulaCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mustafa Erbin on 22.05.2017.
 */

@Service
public class NebulaCacheService implements ICacheService {

    @Autowired
    private NebulaCache nebulaCache;

    public NebulaCacheService() {
    }

    protected NebulaCacheService(NebulaCache nebulaCache) {
        this.nebulaCache = nebulaCache;
    }

    public Object get(Object key){
        return nebulaCache.get(key);
    }

    public void put(Object key, Object value){
        nebulaCache.put(key, value);
    }
}
