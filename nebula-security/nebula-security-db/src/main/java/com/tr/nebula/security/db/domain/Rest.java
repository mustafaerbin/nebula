package com.tr.nebula.security.db.domain;

import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import com.tr.nebula.security.api.domain.NebulaRest;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by  Mustafa Erbin on 12.05.2017.
 */
@Entity
public class Rest extends BaseEntity implements NebulaRest {

    @Column(name = "path")
    private String path;

    @Column(name = "method")
    private String method;

    @Column(name = "nickName")
    private String nickName;

    @Column(name = "description")
    private String description;

    @Column(name = "permissiongroup")
    private String permissiongroup;

    @Column(name = "auth")
    private Boolean auth;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissiongroup() {
        return permissiongroup;
    }

    public void setPermissiongroup(String permissiongroup) {
        this.permissiongroup = permissiongroup;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }
}
