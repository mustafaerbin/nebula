package com.tr.nebula.security.web.controller.model;

import java.io.Serializable;

/**
 * Created by  Mustafa Erbin on 3.04.2017.
 */
public class PermissionModel implements Serializable {

    private String oid;

    private boolean active;

    private String path;

    private String method;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
