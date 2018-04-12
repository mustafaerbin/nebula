package com.tr.nebula.security.ldap.domain;

import com.tr.nebula.security.api.domain.NebulaRole;
import com.tr.nebula.security.api.domain.NebulaUser;

/**
 * Created by Mustafa Erbin on 1.06.2017.
 */
public class User implements NebulaUser {

    private String name;

    private String surname;

    private String uid;

    private String userName;

    private String password;

    @Override
    public Object getUserId() {
        return this.uid;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public NebulaRole getRole() {
        return new Role("Admin", "ADMIN");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSurname() {
        return this.surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append("uid=" + uid);
        buffer.append(", name=" + name);
        buffer.append(", userName=" + userName);
        buffer.append(", password=" + password);
        buffer.append("]");
        return buffer.toString();
    }
}
