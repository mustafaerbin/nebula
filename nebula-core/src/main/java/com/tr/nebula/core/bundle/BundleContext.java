package com.tr.nebula.core.bundle;

/**
 * Created by Mustafa Erbin on 09/03/2017.
 */
public class BundleContext<C> {
    private C context;
    private BundleBeanListener listener;

    public BundleContext(C context, BundleBeanListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public C getContext() {
        return context;
    }

    public BundleBeanListener getListener() {
        return listener;
    }
}
