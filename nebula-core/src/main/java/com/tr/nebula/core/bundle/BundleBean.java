package com.tr.nebula.core.bundle;

/**
 * Created by Mustafa Erbin on 09/03/2017.
 */
public class BundleBean {
    private String name;
    private Object instance;
    public BundleBean(String name, Object instance) {
        this.name = name;
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public Object getInstance() {
        return instance;
    }
}
