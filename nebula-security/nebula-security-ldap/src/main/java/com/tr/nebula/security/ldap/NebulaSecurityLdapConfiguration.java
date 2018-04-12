package com.tr.nebula.security.ldap;

import com.tr.nebula.core.bundle.Configuration;

/**
 * Created by Mustafa Erbin on 2.06.2017.
 */
public class NebulaSecurityLdapConfiguration implements Configuration {

    private String domain = "";

    private String host;

    private String port;

    private String filterDn;

    private String filterFieldName;

    private String userNameAttribute;

    private String otherFilter;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFilterDn() {
        return filterDn;
    }

    public void setFilterDn(String filterDn) {
        this.filterDn = filterDn;
    }

    public String getFilterFieldName() {
        return filterFieldName;
    }

    public void setFilterFieldName(String filterFieldName) {
        this.filterFieldName = filterFieldName;
    }

    public String getUserNameAttribute() {
        return userNameAttribute;
    }

    public void setUserNameAttribute(String userNameAttribute) {
        this.userNameAttribute = userNameAttribute;
    }

    public String getOtherFilter() {
        return otherFilter;
    }

    public void setOtherFilter(String otherFilter) {
        this.otherFilter = otherFilter;
    }
}
