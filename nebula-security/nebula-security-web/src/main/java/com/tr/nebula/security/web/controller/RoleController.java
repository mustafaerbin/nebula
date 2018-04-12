package com.tr.nebula.security.web.controller;

import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.query.search.SearchModel;
import com.tr.nebula.security.api.domain.NebulaRole;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.annotation.PermissionGroup;
import com.tr.nebula.security.db.dao.RoleDao;
import com.tr.nebula.security.db.domain.Role;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Mustafa Erbin on 28.03.2017.
 */
@RestController
@RequestMapping(value = "roles")
public class RoleController {
//    @Autowired
//    private RoleService roleService;

    @Autowired
    private RoleDao roleDao;

    @ApiOperation(value = "Rol Bilgilerini Görüntüleme", nickname = "rolesGET")
    @Auth
    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<? extends NebulaRole> findAll(SearchModel model, HttpServletResponse response) {
        Result<? extends NebulaRole> roleResult = roleDao.findAllStrict(model);
        response.setHeader("X-Total-Count", String.valueOf(roleResult.getTotalCount()));
        return roleResult.getList();
    }

    @ApiOperation(value = "ID ile Rol Bilgisi Görüntüleme", nickname = "rolesOidGET")
    @Auth
    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public Role find(@PathVariable("oid") Long oid) {
        return roleDao.findById(oid);
    }

    @ApiOperation(value = "Rol Kaydı Oluşturma", nickname = "rolesPOST")
    @Auth
    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.POST)
    public Role create(@RequestBody Role role) {
        return roleDao.create(role);
    }

    @ApiOperation(value = "Rol Kaydı Güncelleme", nickname = "rolesPUT")
    @Auth
    @PermissionGroup(name = "rolePermission")
    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
    public Role update(@PathVariable("oid") Long oid, @RequestBody Role role) {
        return roleDao.update(role);
    }

    @ApiOperation(value = "Rol Kaydı Silme", nickname = "rolesDELETE")
    @Auth
    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") Long id) {
        roleDao.delete(id);
    }
}
