package com.tr.nebula.security.api.service;

import com.tr.nebula.security.api.domain.NebulaPermission;
import com.tr.nebula.security.api.domain.NebulaRole;
import com.tr.nebula.security.api.domain.PermissionType;

import java.util.List;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
public interface NebulaPermissionService {

    List<? extends NebulaPermission> findAll();

    List<? extends NebulaPermission> findByPermissionTypeAndRole(PermissionType type, NebulaRole role);

    Integer deleteByRoleAndPermissionType(NebulaRole role, PermissionType permissionType);
}
