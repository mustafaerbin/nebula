package com.tr.nebula.security.db.service;

import com.tr.nebula.persistence.jpa.services.JpaService;
import com.tr.nebula.security.api.domain.NebulaRole;
import com.tr.nebula.security.api.domain.PermissionType;
import com.tr.nebula.security.api.domain.NebulaPermission;
import com.tr.nebula.security.api.service.NebulaPermissionService;
import com.tr.nebula.security.db.domain.Permission;
import com.tr.nebula.security.db.domain.Role;
import com.tr.nebula.security.db.repository.NebulaPermissionRepository;
import com.tr.nebula.security.db.repository.NebulaRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
@Service
public class PermissionService extends JpaService<Permission, Long> implements NebulaPermissionService {

    private NebulaPermissionRepository permissionRepository;

    @Autowired
    private NebulaRoleRepository roleRepository;

    @Autowired
    public PermissionService(NebulaPermissionRepository repository) {
        super(repository);
        this.permissionRepository = repository;
    }

    public List<? extends NebulaPermission> findByPermissionTypeAndRole(PermissionType type, Role role) {
        return permissionRepository.findByPermissionTypeAndRole(type, role);
    }

    @Override
    public List<? extends NebulaPermission> findByPermissionTypeAndRole(PermissionType type, NebulaRole role) {
        List<Role> roles = roleRepository.findByCode(role.getCode());
        return findByPermissionTypeAndRole(type, roles.get(0));
    }

    @Override
    public Integer deleteByRoleAndPermissionType(NebulaRole role, PermissionType type) {
        List<Role> roles = roleRepository.findByCode(role.getCode());
        return permissionRepository.deleteByRoleAndPermissionType(roles.get(0), type);
    }
}
