package com.tr.nebula.security.ldap.domain;

import com.tr.nebula.security.api.domain.NebulaRole;

/**
 * Created by Mustafa Erbin on 2.06.2017.
 */
public class Role implements NebulaRole {

    private String name;

    private String code;

    public Role() {
    }

    public Role(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
