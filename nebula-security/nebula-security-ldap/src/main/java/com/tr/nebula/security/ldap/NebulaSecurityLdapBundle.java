package com.tr.nebula.security.ldap;


import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleContext;
import com.tr.nebula.security.ldap.service.NebulaLdapServiceImpl;

/**
 * Created by Mustafa Erbin on 2.06.2017.
 */
public class NebulaSecurityLdapBundle extends Bundle<NebulaSecurityLdapConfiguration, Object> {
    private static final TypeReference<NebulaSecurityLdapConfiguration> TYPE_REFERENCE = new TypeReference<NebulaSecurityLdapConfiguration>() {
    };

    @Override
    public String getPropertyName() {
        return "nebula.security.ldapConfig";
    }

    @Override
    public TypeReference<NebulaSecurityLdapConfiguration> getTypeReference() {
        return TYPE_REFERENCE;
    }

    @Override
    public void onStart(NebulaSecurityLdapConfiguration configuration, BundleContext<Object> context) {
        try {
            if (configuration != null)
                NebulaLdapServiceImpl.init(configuration.getHost(), configuration.getPort(), configuration.getDomain(), configuration.getFilterDn(), configuration.getFilterFieldName(), configuration.getUserNameAttribute(), configuration.getOtherFilter());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStop(BundleContext<Object> context) {
        NebulaLdapServiceImpl.destroy();
        super.onStop(context);
    }
}
