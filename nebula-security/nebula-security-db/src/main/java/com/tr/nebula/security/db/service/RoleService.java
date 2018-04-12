package com.tr.nebula.security.db.service;

import com.tr.nebula.persistence.jpa.services.JpaService;
import com.tr.nebula.security.api.service.NebulaRoleService;
import com.tr.nebula.security.db.domain.Role;
import com.tr.nebula.security.db.repository.NebulaRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mustafa Erbin on 28.03.2017.
 */
@Service
public class RoleService extends JpaService<Role, Long> implements NebulaRoleService {

    private NebulaRoleRepository nebulaRoleRepository;

    @Autowired
    public RoleService(NebulaRoleRepository repository) {
        super(repository);
        this.nebulaRoleRepository = repository;
    }

    public List<Role> findByCode(String code){
        return nebulaRoleRepository.findByCode(code);
    }
}
