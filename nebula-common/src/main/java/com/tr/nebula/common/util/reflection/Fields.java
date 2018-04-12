package com.tr.nebula.common.util.reflection;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * A class which helps field operations by reflectasm library.
 * It also has com.tr.nebula.cache.config support for class fields.
 */
public class Fields {

    private static final ConcurrentHashMap<Class<?>, ImmutableSet<Field>> cache = new ConcurrentHashMap<>();

    private Fields() {

    }

    private static final ImmutableSet<Field> get(Class<?> clazz) {
        if (!cache.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            cache.put(clazz, ImmutableSet.copyOf(fields));
            return cache.get(clazz);
        }
        return cache.get(clazz);
    }

    public static final <T> void copy(T src, T dest) {
        ImmutableSet<Field> fa = get(src.getClass());
        for (Field field : fa) {
            try {
                field.set(dest, field.get(src));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static final <T> void mergeRight(T src, T dest) {
        ImmutableSet<Field> fa = get(src.getClass());
        for (Field field : fa) {
            try {
                Object value = field.get(src);
                if (value == null)
                    continue;
                field.set(dest, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public static Map<String, Field> getAllFieldsAsMap(Class<?> type) {
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        getAllFieldsAsMap(fieldMap, type, null);
        return fieldMap;
    }

    public static Map<String, Field> getAllFieldsAsMap(Class<?> type, Predicate<Field> predicate) {
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        getAllFieldsAsMap(fieldMap, type, predicate);
        return fieldMap;
    }

    public static void getAllFieldsAsMap(Map<String, Field> fieldMap, Class<?> type, Predicate<Field> predicate) {
        if(predicate != null) {
            for(Field field: type.getDeclaredFields()) {
                if(!predicate.test(field)) continue;
                field.setAccessible(true);
                fieldMap.putIfAbsent(field.getName(), field);
            }
        } else {
            for(Field field: type.getDeclaredFields()) {
                field.setAccessible(true);
                fieldMap.putIfAbsent(field.getName(), field);
            }
        }
        if(type.getSuperclass() != null) {
            getAllFieldsAsMap(fieldMap, type.getSuperclass(), predicate);
        }
    }

    public static  Map<String, Field> findFieldsInMapByPredicate(Map<String, Field> fieldMap, Predicate<Field> predicate) {
        Map<String, Field> foundFieldMap = new LinkedHashMap<>();
        for(Map.Entry<String, Field> entry: fieldMap.entrySet()) {
            if(predicate.test(entry.getValue())) {
                foundFieldMap.put(entry.getKey(), entry.getValue());
            }
        }
        return foundFieldMap;
    }

    public static Object castValue(Field field, String value) {
        return castValue(field.getType(), value);
    }

    public static Object castValue(Class<?> type, String value) {
        if(value == null || "null".equals(value) || (!String.class.equals(type) && "".equals(value))) {
            return null;
        }
        switch (type.getName()) {
            case "java.math.BigDecimal":
                return new BigDecimal(value);
            case "java.lang.Boolean":
            case "boolean":
                return Boolean.parseBoolean(value);
            case "java.lang.Double":
            case "double":
                return Double.parseDouble(value);
            case "java.lang.Integer":
            case "int":
                return Integer.parseInt(value);
            case "java.lang.Long":
            case "long":
                return Long.parseLong(value);
            case "java.lang.String":
                return value;
            case "java.util.Date":
                return new Date(Long.parseLong(value));


        }
        if ((type instanceof Class && (type).isEnum())) {
            return Enum.valueOf((Class<? extends Enum>) type, value);
        }
        return null;
    }


    public static Class<?> getTypeOfList(Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> elementClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        return elementClass;
    }

}