package com.tr.nebula.security.db.domain;

import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import com.tr.nebula.security.api.domain.NebulaRole;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by  Mustafa Erbin on 21.03.2017.
 */
@Entity(name = "n_role")
public class Role extends BaseEntity implements NebulaRole {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
