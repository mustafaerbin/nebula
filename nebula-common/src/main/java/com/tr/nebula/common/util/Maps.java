package com.tr.nebula.common.util;

import java.util.Map;

/**
 * Created by kamilbukum on 15/03/2017.
 */
public class Maps {
    public static <K,V> V getOrDefault(Map<K, V> map, K key, V defaultValue){
        V value = map.get(key);
        return  value != null ? value: defaultValue;
    }

    public static  <K,V> void putIfAbsent(Map<K, V> map, K key, V value) {
        V holdingValue = map.get(key);
        if(holdingValue == null) {
            map.put(key, value);
        }
    }
}
