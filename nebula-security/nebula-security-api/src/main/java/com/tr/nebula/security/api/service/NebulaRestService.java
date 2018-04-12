package com.tr.nebula.security.api.service;

import com.tr.nebula.security.api.domain.NebulaRest;

import java.util.Map;

/**
 * Created by Mustafa Erbin on 12.05.2017.
 */
public interface NebulaRestService {
    Object create(String path, String method, String nickName, String description, String permissionGroup, Boolean auth);
    Object update(NebulaRest rest, String path, String method, String nickName, String description, String permissionGroup, Boolean auth);
    Object findByProperties(Map propertyMap);
}
