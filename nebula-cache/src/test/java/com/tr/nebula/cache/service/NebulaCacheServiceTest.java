package com.tr.nebula.cache.service;

import com.tr.nebula.cache.config.NebulaCache;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mustafa Erbin on 29.05.2017.
 */
@RunWith(SpringRunner.class)
public class NebulaCacheServiceTest {

    private NebulaCacheService nebulaCacheService;

    private NebulaCache nebulaCache;

    @Before
    public void setup(){
        nebulaCache = new NebulaCache();
        nebulaCache.init();
        nebulaCacheService = new NebulaCacheService(nebulaCache);
    }


    @Test
    public void cacheServicePutTest(){

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("string","string");
        objectMap.put("int",1);
        objectMap.put("long",2L);
        objectMap.put("bool", false);
        objectMap.put("double",1.2);

        for(String key : objectMap.keySet()){
            nebulaCacheService.put(key,objectMap.get(key));
        }

        for(String key : objectMap.keySet()){
            Object object = nebulaCacheService.get(key);
            Assertions.assertThat(object).isNotNull();
            Assertions.assertThat(object).isEqualTo(objectMap.get(key));
        }
    }

    @After
    public void destroy(){
        nebulaCacheService = null;
        nebulaCache.destroy();
        nebulaCache = null;
    }
}
