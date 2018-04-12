package com.tr.nebula.security.api.domain;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
public interface NebulaPermission {

//    String getPath();
//
//    String getMethod();

    NebulaRole getRole();

    PermissionType getPermissionType();

    NebulaMenu getMenu();

    NebulaRest getRest();

    String getPermissionGroup();
}
