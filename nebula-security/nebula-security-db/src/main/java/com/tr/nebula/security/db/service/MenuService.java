package com.tr.nebula.security.db.service;

import com.tr.nebula.persistence.jpa.services.JpaService;
import com.tr.nebula.security.api.domain.PermissionType;
import com.tr.nebula.security.api.model.SessionUser;
import com.tr.nebula.security.db.domain.Menu;
import com.tr.nebula.security.db.domain.Permission;
import com.tr.nebula.security.db.domain.Role;
import com.tr.nebula.security.db.domain.model.MenuModel;
import com.tr.nebula.security.db.repository.NebulaMenuRepository;
import com.tr.nebula.security.db.repository.NebulaPermissionRepository;
import com.tr.nebula.security.db.repository.NebulaRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mustafa Erbin on 29.03.2017.
 */
@Service
public class MenuService extends JpaService<Menu, Long> {

    private List<Role> authorities;

    private Set<Permission> permissions;

    private NebulaMenuRepository nebulaMenuRepository;

    @Autowired
    private NebulaPermissionRepository nebulaPermissionRepository;

    @Autowired
    private NebulaRoleRepository nebulaRoleRepository;

    @Autowired
    public MenuService(NebulaMenuRepository repository) {
        super(repository);
        this.nebulaMenuRepository = repository;
    }

    public List<MenuModel> getMenuList() {
        return getMenuList(false);
    }

    public List<MenuModel> getMenuList(boolean all) {
        return getMenuList(all, null);
    }

    public List<Menu> findParents(){
        return nebulaMenuRepository.findByParent_idOrderByIndexAsc(null);
    }

    public List<MenuModel> getMenuList(boolean all, Role role) {
        authorities = null;
        permissions = null;
        List<Menu> parents = nebulaMenuRepository.findByParent_idOrderByIndexAsc(null);
        for (Menu menu : parents)
            loadSubMenus(menu);

        if(!all)
            parents = generatePermittedMenus(parents, role);

        List<MenuModel> retList = convertToModelClass(parents);
        return retList != null ? retList : new ArrayList<>();
    }


    private void loadSubMenus(Menu parentMenu) {
        List<Menu> subMenus = nebulaMenuRepository.findByParent_idOrderByIndexAsc(parentMenu.getId());
        if (subMenus != null && subMenus.size() > 0) {
            for (Menu subMenu : subMenus) {
                if (parentMenu.getItems() == null)
                    parentMenu.setItems(new ArrayList<Menu>());
                subMenu.setModule(parentMenu.getModule()+"/"+subMenu.getModule());
                parentMenu.getItems().add(subMenu);
                loadSubMenus(subMenu);
            }
        }
    }

    private List<MenuModel> convertToModelClass(List<Menu> menuList) {
        List<MenuModel> retList = null;
        for (Menu menu : menuList) {
            if (retList == null)
                retList = new ArrayList<>();
            List<MenuModel> itemList = null;
            if (menu.getItems() != null && menu.getItems().size() > 0)
                itemList = convertToModelClass(menu.getItems());

            MenuModel model = new MenuModel(menu);
            model.setItems(itemList);
            retList.add(model);
        }
        return retList;
    }


    private List<Role> getAuthorities() {
        if(authorities == null){
            authorities = new ArrayList<>();
            SessionUser user = (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                for (GrantedAuthority authority : (List<GrantedAuthority>) user.getAuthorities()) {
                    List<Role> roleList = nebulaRoleRepository.findByCode(authority.getAuthority());
                    if (roleList != null && roleList.size() > 0)
                        authorities.add(roleList.get(0));
                }
            }
        }
        return authorities;
    }

    private Set<Permission> getPermittedMenus(Role role) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return getPermittedMenus(roles);
    }

    private Set<Permission> getPermittedMenus(List<Role> roles) {
        if(permissions == null) {
            permissions = new HashSet<>();
            for (Role role : roles) {
                List<Permission> permissionList = nebulaPermissionRepository.findByPermissionTypeAndRole(PermissionType.MENU, role);
                permissions.addAll(permissionList);
            }
        }
        return permissions;
    }

    private List<Menu> generatePermittedMenus(List<Menu> menus, Role role) {
        List<Menu> retList = new ArrayList<>();
        Set<Permission> permissions = null;
        if(role != null)
            permissions = getPermittedMenus(role);
        else
            permissions = getPermittedMenus(getAuthorities());

        for (Menu menu : menus) {
            boolean find = false;
            for (Permission permission : permissions) {
                if (permission.getMenu().getId().equals(menu.getId())) {
                    find = true;
                    retList.add(menu);
                    //break;
                }
            }
            if (find && menu.getItems() != null && menu.getItems().size() > 0) {
                List<Menu> subList = generatePermittedMenus(menu.getItems(), role);
                if(subList != null && subList.size() > 0) {
                    menu.setItems(subList);
                    //retList.add(menu);
                }
            }
        }
        return retList;
    }

}
