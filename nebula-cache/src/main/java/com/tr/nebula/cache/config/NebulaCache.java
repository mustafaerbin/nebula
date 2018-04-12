package com.tr.nebula.cache.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

/**
 * Created by Ali Kızılırmak on 22.05.2017.
 */

public class NebulaCache implements ICacheService{
    static CacheManager manager;

    public static void init(){
        Configuration configuration = new Configuration().defaultCache(new CacheConfiguration("defaultCache", 1000))
                .cache(new CacheConfiguration("nebula-com.tr.nebula.cache.config", 1000).timeToIdleSeconds(0).timeToLiveSeconds(0));
        manager = CacheManager.create(configuration);
    }

    public static void destroy(){
        manager.shutdown();
    }

    public  Object get(Object key){
        if(null == manager.getCache("nebula-com.tr.nebula.cache.config").get(key)){
            return null;
        }
        Element element = manager.getCache("nebula-com.tr.nebula.cache.config").get(key);
        return element.getObjectValue();
    }

    public  void put(Object key, Object value){
        manager.getCache("nebula-com.tr.nebula.cache.config").put(new Element(key,value));
    }
}
