package com.tr.nebula.security.db.repository;

import com.tr.nebula.security.db.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Mustafa Erbin on 28.03.2017.
 */
public interface NebulaRoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByCode(String code);
}
