package com.tr.nebula.cache;

import com.tr.nebula.cache.config.NebulaCache;
import com.tr.nebula.common.lang.TypeReference;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleContext;

/**
 * Created by Ali Kızılırmak on 24.05.2017.
 */
public class NebulaCacheBundle extends Bundle<NebulaCacheConfiguration, Object>{
    private static final TypeReference<NebulaCacheConfiguration> TYPE_REFERENCE = new TypeReference<NebulaCacheConfiguration>() {
    };

    @Override
    public String getPropertyName(){
        return "";
    }

//    @Override
//    public TypeReference<NebulaCacheConfiguration> getTypeReference(){
//        return TYPE_REFERENCE;
//    }

    @Override
    public void onStart(NebulaCacheConfiguration configuration, BundleContext<Object> context){
        NebulaCache.init();
    }

    @Override
    public void onStop(BundleContext<Object> context){
        NebulaCache.destroy();
    }
}
