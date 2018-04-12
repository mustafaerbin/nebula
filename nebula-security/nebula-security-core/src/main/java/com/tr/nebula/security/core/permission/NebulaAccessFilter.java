package com.tr.nebula.security.core.permission;


import com.tr.nebula.security.api.domain.NebulaPermission;
import com.tr.nebula.security.api.domain.NebulaRole;
import com.tr.nebula.security.api.domain.PermissionType;
import com.tr.nebula.security.api.domain.NebulaRest;
import com.tr.nebula.security.api.model.SessionUser;
import com.tr.nebula.security.api.service.NebulaPermissionService;
import com.tr.nebula.security.api.service.NebulaRestService;
import com.tr.nebula.security.api.service.NebulaRoleService;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.annotation.PermissionGroup;
import com.tr.nebula.security.core.cache.SecurityCache;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Mustafa Erbin on 27.03.2017.
 */
public class NebulaAccessFilter extends GenericFilterBean {

    /**
     *
     */
    @Autowired
    private NebulaPermissionService permissionService;

    /**
     *
     */
    @Autowired
    private NebulaRoleService roleService;

    /**
     *
     */
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private NebulaRestService restService;
    /**
     *
     */
    List<? extends NebulaPermission> permissionList;

    /**
     *
     */
    private static String restPath;

    /**
     *
     */
    private static final AntPathMatcher mathcer = new AntPathMatcher();

    private static boolean restLoaded = false;

    /**
     * Filter rest requests using role permissions
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        loadPermissionList();
        refreshRestTable();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();
        if(request.getContextPath() != null && request.getContextPath().length() > 1) {
            requestUri = requestUri.substring(request.getContextPath().length());
        }
        if (requestUri.indexOf(getRestPath()) > -1 && requestUri.indexOf(getRestPath() + "login") < 0) {
            if (permissionList != null && !hasPermitted(requestUri, request.getMethod())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission Denied");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        System.out.println("NebulaAccessFilter destroy");
    }

    /**
     * loadpermissionlist for granted authority
     */
    private void loadPermissionList() {
        permissionList = (List<? extends NebulaPermission>) SecurityCache.get("permissionList#" + getAuthorityCode());
        if (permissionList == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof SessionUser) {
                SessionUser user = (SessionUser) authentication.getPrincipal();
                for (SimpleGrantedAuthority authority : (List<SimpleGrantedAuthority>) user.getAuthorities()) {
                    NebulaRole role = getRole(authority.getAuthority());
                    if (role != null) {
                        if (permissionList == null)
                            permissionList = permissionService.findByPermissionTypeAndRole(PermissionType.REST, role);
                    }
                }
                SecurityCache.put("permissionList#"+getAuthorityCode(), permissionList);
            }
        }
    }

    /**
     *
     * @param url
     * @param method
     * @return
     */
    private boolean hasPermitted(String url, String method) {
        boolean ret = true;
        if (isAuthPath(url, method) && permissionList != null) {
            for (NebulaPermission permission : permissionList) {
                if (permission.getRest() != null) {
                    if (mathcer.match(permission.getRest().getPath(),url) && permission.getRest().getMethod().equals(method)) { //url.indexOf(permission.getPath()) > -1 &&
                        return true;
                    }
                } else {
                    List<Map<String, String>> matchedPaths = getMatchedPaths(permission.getPermissionGroup());
                    for (Map<String, String> matchedPath : matchedPaths) {
                        String mPath = matchedPath.get("path");
                        String mMethod = matchedPath.get("method");
                        if (mathcer.match(mPath,url) && method.equals(mMethod)) {
                            return true;
                        }
                    }
                }
                ret = false;
            }
        }
        return ret;
    }

    /**
     *
     * @param code
     * @return
     */
    private NebulaRole getRole(String code) {
        List<? extends NebulaRole> roles = roleService.findByCode(code);
        return roles.size() > 0 ? roles.get(0) : null;
    }

    /**
     *
     * @param permissionGroup
     * @return
     */
    private List<Map<String, String>> getMatchedPaths(String permissionGroup) {
        List<Map<String, String>> matchedPaths = new ArrayList<>();
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("auth", true);
        propertyMap.put("permissiongroup", permissionGroup);
        List<? extends NebulaRest> restServices = (List<? extends NebulaRest>)restService.findByProperties(propertyMap);
        for (NebulaRest rest : restServices) {
            Map<String, String> pathMap = new HashMap<>();
            pathMap.put("path", rest.getPath());
            pathMap.put("method", rest.getMethod());
            matchedPaths.add(pathMap);
        }
        return matchedPaths;
    }

    /**
     *
     * @return
     */
    public static String getRestPath() {
        return restPath;
    }

    /**
     *
     * @param restPath
     */
    public static void setRestPath(String restPath) {
        NebulaAccessFilter.restPath = restPath;
    }

    /**
     *
     * @return authenticated user
     */
    private static SessionUser getSessionUser(){
        SessionUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionUser)
            user = (SessionUser) authentication.getPrincipal();

        return user;
    }

    /**
     *
     * @return authority code
     */
    private static String getAuthorityCode(){
        if(getSessionUser() != null) {
            SimpleGrantedAuthority authority = (SimpleGrantedAuthority) getSessionUser().getAuthorities().get(0);
            return authority.getAuthority();
        }
        return "";
    }

    private boolean isAuthPath(String url, String method){
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("auth", true);
        propertyMap.put("path", url);
        propertyMap.put("method", method);
        List<? extends NebulaRest> restServices = (List<? extends NebulaRest>)restService.findByProperties(propertyMap);
        return restServices != null && restServices.size() > 0;
//        Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
//        for (RequestMappingInfo info : requestMappingInfos) {
//            String path = (String) info.getPatternsCondition().getPatterns().toArray()[0];
//            if(mathcer.match((getRestPath() + path).replace("//","/"),url)){
//                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
//                if(rMethod.getAnnotation(Auth.class) != null) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }


    private void refreshRestTable(){
        if(!restLoaded){
            Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
            for(RequestMappingInfo info : requestMappingInfos){
                String path = (String) info.getPatternsCondition().getPatterns().toArray()[0];
                String method = "";
                if(info.getMethodsCondition().getMethods().toArray().length > 0)
                    method = ((RequestMethod) info.getMethodsCondition().getMethods().toArray()[0]).name();

                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
                boolean auth = rMethod.getAnnotation(Auth.class) != null;
                String permissionGroup = rMethod.getAnnotation(PermissionGroup.class) != null ? rMethod.getAnnotation(PermissionGroup.class).name() : "";

                ApiOperation operation = rMethod.getAnnotation(ApiOperation.class);

                String nickName  = operation != null ? operation.nickname() : "";
                String description = operation != null ? operation.value() : "";

                if(!path.equals("") && !method.equals("")){
                    Map<String, Object> propertyMap = new HashMap<>();
                    path = (getRestPath() + path).replace("//","/");
                    propertyMap.put("path", path);
                    propertyMap.put("method", method);

                    List<? extends NebulaRest> restServices = (List<? extends NebulaRest>)restService.findByProperties(propertyMap);
                    if(restServices.size() <= 0){
                        restService.create(path,method, nickName, description, permissionGroup, auth);
                    } else{
                        NebulaRest rest = restServices.get(0);
                        restService.update(rest, path,method, nickName, description, permissionGroup, auth);
                    }
                }
            }
            restLoaded = true;
        }
    }


}
