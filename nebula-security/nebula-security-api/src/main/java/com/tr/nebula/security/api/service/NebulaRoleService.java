package com.tr.nebula.security.api.service;

import com.tr.nebula.security.api.domain.NebulaRole;

import java.util.List;

/**
 * Created by Mustafa Erbin on 5.04.2017.
 */
public interface NebulaRoleService {
    List<? extends NebulaRole> findByCode(String code);
}
