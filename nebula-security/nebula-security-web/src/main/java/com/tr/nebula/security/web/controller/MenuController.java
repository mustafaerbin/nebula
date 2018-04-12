package com.tr.nebula.security.web.controller;

import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.query.search.SearchModel;
import com.tr.nebula.security.api.domain.NebulaMenu;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.annotation.PermissionGroup;
import com.tr.nebula.security.core.cache.SecurityCache;
import com.tr.nebula.security.db.dao.MenuDao;
import com.tr.nebula.security.db.domain.Menu;
import com.tr.nebula.security.db.domain.model.MenuModel;
import com.tr.nebula.security.db.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Mustafa Erbin on 29.03.2017.
 */
@RestController
@RequestMapping(value = "menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @ApiOperation(value = "Menü Listesi Görüntüleme", nickname = "menusAllGET")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<? extends NebulaMenu> findAll(SearchModel model, HttpServletResponse response) {
        Result<Menu> menuResult = menuDao.findAllStrict(model);
        response.setHeader("X-Total-Count", String.valueOf(menuResult.getTotalCount()));
        return menuResult.getList();
    }

    @ApiOperation(value = "Menü Ağacı Görüntüleme", nickname = "menusGET")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<MenuModel> findAllSessionMenu() {
        return menuService.getMenuList();
    }

    @ApiOperation(value = "ID ile Menü Bilgisi Görüntüleme", nickname = "menusOidGET")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public Menu find(@PathVariable("oid") String oid) {
        return menuDao.findById(oid);
    }

    @ApiOperation(value = "Menü Kaydı Oluşturma", nickname = "menusPOST")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.POST)
    public Menu create(@RequestBody Menu menu) {
        SecurityCache.put("permissionMenuList", null);
        return menuDao.create(menu);
    }

    @ApiOperation(value = "Menü Kaydı Güncelleme", nickname = "menusPUT")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
    public Menu update(@PathVariable("oid") String oid, @RequestBody Menu role) {
        SecurityCache.put("permissionMenuList", null);
        return menuDao.update(role);
    }

    @ApiOperation(value = "Menü Kaydı Silme", nickname = "menusDELETE")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") Long id) {
        SecurityCache.put("permissionMenuList", null);
        menuDao.delete(id);
    }

    @ApiOperation(value = "Üst Menüleri Görüntüleme", nickname = "menusParentsGET")
    @Auth
    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "parents", method = RequestMethod.GET)
    public List<? extends NebulaMenu> getParents() {
        return menuService.findParents();
    }

}
