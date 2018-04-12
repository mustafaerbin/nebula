package com.tr.nebula.security.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleContext;
import com.tr.nebula.security.core.cache.SecurityCache;
import com.tr.nebula.security.core.config.NebulaSecurityConfig;
import com.tr.nebula.security.core.filter.NebulaUsernamePasswordAuthenticationFilter;
import com.tr.nebula.security.core.permission.NebulaAccessFilter;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public class NebulaSecurityBundle extends Bundle<NebulaSecurityConfiguration, Object> {

    private static final TypeReference<NebulaSecurityConfiguration> TYPE_REFERENCE = new TypeReference<NebulaSecurityConfiguration>() {
    };

    @Override
    public String getPropertyName() {
        return "nebula.security";
    }

    @Override
    public TypeReference<NebulaSecurityConfiguration> getTypeReference() {
        return TYPE_REFERENCE;
    }

    @Override
    public void onStart(NebulaSecurityConfiguration configuration, BundleContext<Object> context) {
        NebulaSecurityConfig.setRestPath(configuration.getPath());
        NebulaSecurityConfig.setPermittedPaths(configuration.getPermittedPaths());
        NebulaUsernamePasswordAuthenticationFilter.setLoginUrl(configuration.getPath() + "/login");
        NebulaAccessFilter.setRestPath(configuration.getPath() + "/");
        SecurityCache.init();
        SecurityCache.put("authType",configuration.getAuthType());
    }

    @Override
    public void onStop(BundleContext<Object> context) {
        SecurityCache.destroy();
        super.onStop(context);
    }
}
