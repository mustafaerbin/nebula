package com.tr.nebula.security.core;

import com.tr.nebula.core.bundle.Configuration;

import java.util.HashMap;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public class NebulaSecurityConfiguration implements Configuration {
    private String path;
    private String authType;
    private String[] permittedPaths;
    private HashMap<String, String> ldapConfig;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String[] getPermittedPaths() {
        return permittedPaths;
    }

    public void setPermittedPaths(String[] permittedPaths) {
        this.permittedPaths = permittedPaths;
    }

    public HashMap<String, String> getLdapConfig() {
        return ldapConfig;
    }

    public void setLdapConfig(HashMap<String, String> ldapConfig) {
        this.ldapConfig = ldapConfig;
    }
}
