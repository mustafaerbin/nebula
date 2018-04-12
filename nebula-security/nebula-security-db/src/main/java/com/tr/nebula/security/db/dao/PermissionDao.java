package com.tr.nebula.security.db.dao;

import com.tr.nebula.persistence.jpa.dao.BaseDaoImpl;
import com.tr.nebula.security.db.domain.Permission;
import org.springframework.stereotype.Service;

/**
 * Created by Mustafa Erbin on 27.04.2017.
 */
@Service
public class PermissionDao extends BaseDaoImpl<Permission, Long> {
}
