package com.tr.nebula.security.api.domain;

/**
 * Created by Mustafa Erbin on 12.05.2017.
 */
public interface NebulaRest {

    String getPath();

    void setPath(String path);

    String getMethod();

    void setMethod(String method);

    String getNickName();

    void setNickName(String nickName);

    String getDescription();

    void setDescription(String description);

    String getPermissiongroup();

    void setPermissiongroup(String permissiongroup);

    Boolean getAuth();

    void setAuth(Boolean auth);
}
