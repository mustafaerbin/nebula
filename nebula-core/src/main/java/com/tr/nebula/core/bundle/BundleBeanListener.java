package com.tr.nebula.core.bundle;

/**
 * Created by kamilbukum on 09/03/2017.
 */
public interface BundleBeanListener {
    void onCreate(BundleBean bean);

    void destroy(BundleBean bean);
}