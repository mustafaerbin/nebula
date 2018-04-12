package com.tr.nebula.security.web.controller;

import com.tr.nebula.security.api.domain.NebulaPermission;
import com.tr.nebula.security.api.domain.PermissionType;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.annotation.PermissionGroup;
import com.tr.nebula.security.core.cache.SecurityCache;
import com.tr.nebula.security.db.dao.RestDao;
import com.tr.nebula.security.db.domain.Permission;
import com.tr.nebula.security.db.domain.Rest;
import com.tr.nebula.security.db.domain.Role;
import com.tr.nebula.security.db.domain.model.MenuModel;
import com.tr.nebula.security.db.service.MenuService;
import com.tr.nebula.security.db.service.PermissionService;
import com.tr.nebula.security.db.service.RoleService;
import com.tr.nebula.security.web.controller.model.EndPointTreeModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Mustafa Erbin on 28.03.2017.
 */
@RestController
@RequestMapping(value = "permissions")
public class PermissionController {

    @Value("${nebula.security.path:restPath}")
    private String restPath;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private RestDao restDao;

//    @Auth
//    @PermissionGroup(name = "permissionController")
//    @RequestMapping(method = RequestMethod.GET)
//    public List<? extends NebulaPermission> findAll() {
//        return permissionService.findAll();
//    }
//
//    @Auth
//    @PermissionGroup(name = "permissionController")
//    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
//    public Permission find(@PathVariable("oid") Long oid) {
//        return permissionService.findOne(oid);
//    }
//
//    @Auth
//    @PermissionGroup(name = "permissionController")
//    @RequestMapping(method = RequestMethod.POST)
//    public Permission create(@RequestBody Permission user) {
//        SecurityCache.put("permissionList", null);
//        return permissionService.create(user);
//    }
//
//    @Auth
//    @PermissionGroup(name = "permissionController")
//    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
//    public Permission update(@PathVariable("oid") Long oid, @RequestBody Permission user) {
//        SecurityCache.put("permissionList", null);
//        return permissionService.update(user, oid);
//    }
//
//    @Auth
//    @PermissionGroup(name = "permissionController")
//    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
//    public void delete(@PathVariable("oid") Long id) {
//        SecurityCache.put("permissionList", null);
//        permissionService.delete(id);
//    }

    @ApiOperation(value = "Tüm Servisleri Görüntüleme", nickname = "permissionEndPointsGET")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/endPoints")
    public List restList(HttpServletRequest request, HttpServletResponse response) {
        List<EndPointTreeModel> retList = (List<EndPointTreeModel>) SecurityCache.get("endPoints");
        if (retList == null) {
            retList = new ArrayList<>();
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("auth",true);
            List<Rest> restList = restDao.findByProperties(propertyMap);
            //Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
            for (Rest rest : restList) {
                String group = rest.getPermissiongroup();
                String path = rest.getPath();
                String method = rest.getMethod();
                String nickName = rest.getNickName();
                String description = rest.getDescription();
//                String group = "";
//                String path = "";
//                RequestMethod method = null;
//                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
//                if (rMethod.getAnnotation(Auth.class) != null) {
//                    if (rMethod.getAnnotation(PermissionGroup.class) != null)
//                        group = rMethod.getAnnotation(PermissionGroup.class).name();
//
//                    String nickName = "";
//                    String description = "";
//                    if (rMethod.getAnnotation(ApiOperation.class) != null) {
//                        nickName = rMethod.getAnnotation(ApiOperation.class).nickname();
//                        description = rMethod.getAnnotation(ApiOperation.class).value();
//                    }
//
//                    path = (String) info.getPatternsCondition().getPatterns().toArray()[0];
//
//                    path = restPath + path;
//
//                    if (info.getMethodsCondition().getMethods().toArray().length > 0)
//                        method = (RequestMethod) info.getMethodsCondition().getMethods().toArray()[0];

                    if (method != null) {
                        if (group.equals("")) {
                            EndPointTreeModel model = new EndPointTreeModel();
                            model.setText(!description.equals("") ? description : method + " " + path);
                            //model.setCode(!nickName.equals("") ? nickName : method + "#" + path);
                            model.setCode(rest.getId());
                            model.setChildren(new ArrayList<>());
                            retList.add(model);
                        } else {
                            configurePermissionGroup(retList, group, path, method, nickName, description, rest.getId());
                        }
                    }
            }
            SecurityCache.put("endPoints", retList);
        }
        return retList;
    }

    @ApiOperation(value = "Rol Bilgisine Ait Servisleri Görüntüleme", nickname = "permissionEndPointsRoleGET")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/endPoints/{oid}")
    public List restListByRoleId(@PathVariable("oid") Long oid, HttpServletRequest request, HttpServletResponse response) {
        Role role = roleService.findOne(oid);
        List<Object> retList = (List<Object>) SecurityCache.get("permissionEndpointList#" + role.getCode());
        if (retList == null) {
            retList = new ArrayList<>();
            List<? extends NebulaPermission> permissions = permissionService.findByPermissionTypeAndRole(PermissionType.REST, role);
            for (Permission permission : (List<Permission>) permissions) {
                String permissionStr = "";//permission.getRest().getPath();
                if (permission.getRest() != null) {
                    retList.add(permission.getRest().getId());
                } else if(permission.getPermissionGroup() != null) {
                    retList.add(permission.getPermissionGroup());
                    retList.addAll(getSubCodes(permission.getPermissionGroup()));
                }
            }
            SecurityCache.put("permissionEndpointList#" + role.getCode(), retList);
        }

        return retList;
    }

    @ApiOperation(value = "Rol Bilgisine Servis Yetkisi Verme", nickname = "permissionEndPointsRolePOST")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.POST, value = "/configurePermission/{oid}")
    public List configurePermission(@PathVariable("oid") Long roleOid, @RequestBody ArrayList<Object> model,
                                    HttpServletRequest request, HttpServletResponse response) {

        Role role = roleService.findOne(roleOid);
        permissionService.deleteByRoleAndPermissionType(role, PermissionType.REST);
        Permission permission;
        for (Object code : model) {
            permission = new Permission();
            permission.setRole(role);
            permission.setPermissionType(PermissionType.REST);
            if(code instanceof Integer){
                permission.setRest(restDao.findById(((Integer)code).longValue()));
            } else if(code instanceof String){
                permission.setPermissionGroup((String) code);
            }
            permissionService.create(permission);
        }

        SecurityCache.put("permissionEndpointList#" + role.getCode(), null);
        SecurityCache.put("permissionList#" + role.getCode(), null);

        return model;
    }

    @ApiOperation(value = "Yetki Verilecek Tüm Menüleri Görüntüleme", nickname = "permissionMenusGET")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/menus")
    public List menuList(HttpServletRequest request, HttpServletResponse response) {
        List<EndPointTreeModel> retList = (List<EndPointTreeModel>) SecurityCache.get("permissionMenuList");
        if (retList == null) {
            retList = converToPermissionModel(menuService.getMenuList(true));
            SecurityCache.put("permissionMenuList", retList);
        }
        return retList;
    }

    @ApiOperation(value = "Rol Bilgisine Ait Menüleri Görüntüleme", nickname = "permissionMenusRoleGET")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/menus/{oid}")
    public List menuListForRole(@PathVariable("oid") Long oid, HttpServletRequest request, HttpServletResponse response) {
        Role role = roleService.findOne(oid);
        List<Long> retList = (List<Long>) SecurityCache.get("permissionMenuList#" + role.getCode());
        if (retList == null) {
            List<MenuModel> menuList = menuService.getMenuList(false, role);
            retList = getAuthorizedMenus(menuList);
            SecurityCache.put("permissionMenuList#" + role.getCode(), retList);
        }
        return retList;
    }

    @ApiOperation(value = "Rol Bilgisine Menü Yetkisi Verme", nickname = "permissionMenusRolePOST")
    @Auth
    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.POST, value = "/configuresMenu/{oid}")
    public List menuListOid(@PathVariable("oid") Long roleOid, @RequestBody ArrayList<Long> model,
                            HttpServletRequest request, HttpServletResponse response) {
        Role role = roleService.findOne(roleOid);
        permissionService.deleteByRoleAndPermissionType(role, PermissionType.MENU);
        Permission permission;
        for (Long code : model) {
            permission = new Permission();
            permission.setRole(role);
            permission.setPermissionType(PermissionType.MENU);
            permission.setMenu(menuService.findOne(code));

            permissionService.create(permission);
        }

        SecurityCache.put("permissionMenuList#" + role.getCode(), null);

        return model;
    }

    private List<Long> getAuthorizedMenus(List<MenuModel> menuList) {
        List<Long> retList = new ArrayList<>();
        for (MenuModel model : menuList) {
            retList.add(model.getId());
            if (model.getItems() != null && model.getItems().size() > 0)
                retList.addAll(getAuthorizedMenus((List<MenuModel>) model.getItems()));
        }
        return retList;
    }

    private List<EndPointTreeModel> converToPermissionModel(List<MenuModel> menuList) {
        List<EndPointTreeModel> retList = new ArrayList<>();
        EndPointTreeModel model;
        for (MenuModel menuModel : menuList) {
            model = new EndPointTreeModel();
            model.setText(menuModel.getText());
            model.setCode(menuModel.getId());
            if (menuModel.getItems() != null) {
                model.setChildren(converToPermissionModel((List<MenuModel>) menuModel.getItems()));
            } else {
                model.setChildren(new ArrayList<>());
            }
            retList.add(model);
        }
        return retList;
    }

    private void configurePermissionGroup(List<EndPointTreeModel> modelList, String permissionGroup, String path, String method, String nickName, String description, Long id) {
        boolean find = false;
        EndPointTreeModel addedModel = new EndPointTreeModel();
        addedModel.setText(!description.equals("") ? description : method + " " + path);
        //addedModel.setCode(!nickName.equals("") ? nickName : method + "#" + path);
        addedModel.setCode(id);
        addedModel.setChildren(new ArrayList<>());

        for (EndPointTreeModel model : modelList) {
            if (model.getCode().equals(permissionGroup)) {
                find = true;
                if (model.getChildren() == null)
                    model.setChildren(new ArrayList<>());
                model.getChildren().add(addedModel);
                break;
            }
        }

        if (!find) {
            EndPointTreeModel groupModel = new EndPointTreeModel();
            groupModel.setText(permissionGroup);
            groupModel.setCode(permissionGroup);
            groupModel.setChildren(new ArrayList<>());
            groupModel.getChildren().add(addedModel);
            modelList.add(groupModel);
        }
    }

    private List<Object> getSubCodes(String permissionGroup) {
        List<EndPointTreeModel> modelList = (List<EndPointTreeModel>) SecurityCache.get("endPoints");
        List<Object> retList = new ArrayList<>();
        if (modelList != null) {
            for (EndPointTreeModel model : modelList) {
                if (model.getCode().equals(permissionGroup)) {
                    for (EndPointTreeModel subModel : model.getChildren()) {
                        retList.add(subModel.getCode());
                    }
                    break;
                }
            }
        }

        return retList;
    }

    private String findRestByNickName(String nickName) {
        String result = (String) SecurityCache.get(nickName);
        if (result == null) {
            Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
            for (RequestMappingInfo info : requestMappingInfos) {
                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
                if (rMethod.getAnnotation(ApiOperation.class) != null && rMethod.getAnnotation(ApiOperation.class).nickname().equals(nickName)) {
                    result = restPath + info.getPatternsCondition().getPatterns().toArray()[0];
                    if (info.getMethodsCondition().getMethods().toArray().length > 0)
                        result = ((RequestMethod) info.getMethodsCondition().getMethods().toArray()[0]).name() + "#" + result;

                    SecurityCache.put(nickName, result);
                    break;
                }
            }
        }

        return result != null ? result : nickName;
    }

    private String findNickNameByRest(String url, String method) {
        String result = "";
        Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
        for (RequestMappingInfo info : requestMappingInfos) {
            if ((restPath + info.getPatternsCondition().getPatterns().toArray()[0]).equals(url) && ((RequestMethod) info.getMethodsCondition().getMethods().toArray()[0]).name().equals(method)) {
                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
                if (rMethod.getAnnotation(ApiOperation.class) != null) {
                    result = rMethod.getAnnotation(ApiOperation.class).nickname();
                }
                break;
            }
        }
        return result;
    }

}
