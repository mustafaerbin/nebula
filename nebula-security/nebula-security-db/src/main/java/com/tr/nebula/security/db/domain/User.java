package com.tr.nebula.security.db.domain;

import com.tr.nebula.persistence.api.criteria.ann.SearchFrom;
import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import com.tr.nebula.security.api.domain.NebulaUser;

import javax.persistence.*;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
@Entity(name = "n_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements NebulaUser {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToOne
    @SearchFrom(entity = Role.class)
    private Role role;

    @Override
    public Long getUserId() {
        return getId();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Role getRole() {
        return role;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
