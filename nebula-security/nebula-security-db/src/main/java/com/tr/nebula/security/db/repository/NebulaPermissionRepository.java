package com.tr.nebula.security.db.repository;

import com.tr.nebula.security.api.domain.PermissionType;
import com.tr.nebula.security.db.domain.Permission;
import com.tr.nebula.security.db.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
@Repository
public interface NebulaPermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByPermissionTypeAndRole(PermissionType type, Role role);

    @Modifying
    @Transactional
    @Query(value="delete from Permission c where c.role = ?1 and c.permissionType = ?2")
    Integer deleteByRoleAndPermissionType(Role role, PermissionType type);
}
