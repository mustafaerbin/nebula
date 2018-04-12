package com.tr.nebula.security.db.service;

import com.tr.nebula.security.api.domain.NebulaRest;
import com.tr.nebula.security.api.service.NebulaRestService;
import com.tr.nebula.security.db.dao.RestDao;
import com.tr.nebula.security.db.domain.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Mustafa Erbin on 12.05.2017.
 */
@Service
public class RestService implements NebulaRestService {

    @Autowired
    private RestDao restDao;

    public Rest create(String path, String method, String nickName, String description, String permissionGroup, Boolean auth) {
        Rest rest = new Rest();
        rest.setPath(path);
        rest.setMethod(method);
        rest.setNickName(nickName);
        rest.setDescription(description);
        rest.setPermissiongroup(permissionGroup);
        rest.setAuth(auth);
        return restDao.create(rest);
    }

    public Rest update(NebulaRest rest, String path, String method, String nickName, String description, String permissionGroup, Boolean auth) {
        //Rest rest = restDao.findById(id);
        rest.setPath(path);
        rest.setMethod(method);
        rest.setNickName(nickName);
        rest.setDescription(description);
        rest.setPermissiongroup(permissionGroup);
        rest.setAuth(auth);
        return restDao.update((Rest)rest);
    }

    @Override
    public List findByProperties(Map propertyMap) {
        return restDao.findByProperties(propertyMap);
    }
}
