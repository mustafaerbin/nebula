package com.tr.nebula.core.bundle;

/**
 * Created by kamilbukum on 09/03/2017.
 */
public class BundleType {
    private boolean list = false;
    private Class<?> type;

    public BundleType(Class<?> type, boolean list){
        this.type = type;
        this.list = list;
    }

    public boolean isList() {
        return list;
    }

    public Class<?> getType() {
        return type;
    }
}
