package com.tr.nebula.security.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tr.nebula.security.api.domain.NebulaUser;
import com.tr.nebula.security.api.model.SessionUser;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/**
 * Created by  Mustafa Erbin on 21.03.2017.
 */
public class SessionUserImpl extends org.springframework.security.core.userdetails.User implements SessionUser {

    private NebulaUser nebulaUser;

    public SessionUserImpl(NebulaUser nebulaUser){
        super(nebulaUser.getUsername(), nebulaUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(nebulaUser.getRole() != null ? nebulaUser.getRole().getCode() : ""));
        this.nebulaUser = nebulaUser;
    }


    @JsonIgnore
    @Override
    public NebulaUser getUser() {
        return nebulaUser;
    }

    @Override
    public Object getId() {
        return nebulaUser.getUserId();
    }

    @Override
    public String getUsername() {
        return nebulaUser.getUsername();
    }

    @Override
    public List getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(nebulaUser.getRole() != null ? nebulaUser.getRole().getCode() : "");
    }

//    private static String getPermissionGroups(NebulaUser user){
//        String groups = "";
//        for(NebulaRole permission : user.getRole()){
//            if(groups.length() > 0)
//                groups += ",";
//            groups += permission.getCode();
//        }
//        return groups;
//    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getName() {
        return nebulaUser.getName();
    }

    @Override
    public String getSurname() {
        return nebulaUser.getSurname();
    }
}
