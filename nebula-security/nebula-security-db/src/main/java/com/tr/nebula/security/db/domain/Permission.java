package com.tr.nebula.security.db.domain;

import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import com.tr.nebula.security.api.domain.NebulaPermission;
import com.tr.nebula.security.api.domain.PermissionType;

import javax.persistence.*;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
@Entity
public class Permission extends BaseEntity implements NebulaPermission {

//    @Column(length = 100)
//    private String path;
//
//    @Column
//    private String method;

    @ManyToOne
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Rest rest;

    @Column
    private String permissionGroup;

//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getMethod() {
//        return method;
//    }
//
//    public void setMethod(String method) {
//        this.method = method;
//    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Rest getRest() {
        return rest;
    }

    public void setRest(Rest rest) {
        this.rest = rest;
    }

    public String getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(String permissionGroup) {
        this.permissionGroup = permissionGroup;
    }
}
