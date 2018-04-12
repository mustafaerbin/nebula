package com.tr.nebula.security.web.controller;

import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.query.search.SearchModel;
import com.tr.nebula.security.api.domain.NebulaUser;
import com.tr.nebula.security.api.model.SessionUser;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.annotation.PermissionGroup;
import com.tr.nebula.security.db.dao.UserDao;
import com.tr.nebula.security.db.domain.User;
import com.tr.nebula.security.db.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Mustafa Erbin on 9.03.2017.
 */
@RestController
@RequestMapping(value = "users")
public class UserController {
//    @Autowired
//    private UserService userService;

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private UserDao2 userDao2;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "Kullanıcı Listesi Görüntüleme", nickname = "usersGET")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll(SearchModel model, HttpServletResponse response) {
        Result<User> userResult = userDao.findAllStrict(model);
        response.setHeader("X-Total-Count", String.valueOf(userResult.getTotalCount()));
        return userResult.getList();
    }

    @ApiOperation(value = "ID İle Kullanıcı Bilgisi Görüntüleme", nickname = "usersGETID")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public User find(@PathVariable("oid") Long oid) {
        return userDao.findById(oid);
    }

    @ApiOperation(value = "Kullanıcı Oluşturma", nickname = "usersPOST")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return userDao.create(user);
    }

    @ApiOperation(value = "Kullanıcı Güncelleme", nickname = "usersPUT")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
    public User update(@PathVariable("oid") String oid, @RequestBody User user) {
        return userDao.update(user);
    }

    @ApiOperation(value = "Kullanıcı Silme", nickname = "usersPUT")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") Long id) {
        userDao.delete(id);
    }

    @ApiOperation(value = "Kullanıcı Listeleme", nickname = "usersSelectGET")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Result<Map<String, Object>> selection(SearchModel model) {
        String[] fields = {"oid", "username", "active", "role.name"};
        model.setFields(fields);
        return userDao.findAll(model);
    }

    @ApiOperation(value = "Kullanıcı Entity Bilgilerini Görüntüleme", nickname = "usersPropertiesGET")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    public List properties() {
        return userDao.getProperties();
    }

    @ApiOperation(value = "Login Olan Kullanıcı Bilgilerini Görüntüleme", nickname = "usersSessionUserGET")
    @Auth
    @PermissionGroup(name = "userPermission")
    @RequestMapping(value = "/sessionUser", method = RequestMethod.GET)
    public NebulaUser sessionUser(SessionUser sessionUser) {
        return sessionUser.getUser();
    }

}
