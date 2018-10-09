package com.tr.nebula.core.bundle;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by Mustafa Erbin on 09/03/2017.
 */
public abstract class Bundle<P, S> {
    private Boolean start = false;
    private Boolean stop = false;

    public String name() {
        return this.getClass().getName();
    }

    public String getPropertyName(){
        return null;
    }

    public TypeReference<P> getTypeReference(){
        return null;
    }

    public Class<P> getType(){
        return null;
    }

    public void onStartBundle(P configuration, BundleContext<S> context) {
        synchronized (start) {
            if (!start) {
                start = true;
                this.onStart(configuration, context);
            }
        }
    }
    public void onStart(P configuration, BundleContext<S> context) {

    }

    public void onStopBundle(BundleContext<S> context) {
        synchronized (stop) {
            if (!stop) {
                stop = true;
                this.onStop(context);
            }
        }
    }

    public void onStop(BundleContext<S> context) {

    }
}
