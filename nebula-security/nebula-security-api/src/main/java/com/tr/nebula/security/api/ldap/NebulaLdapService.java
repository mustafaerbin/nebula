package com.tr.nebula.security.api.ldap;

import com.tr.nebula.security.api.domain.NebulaUser;

/**
 * Created by Mustafa Erbin on 1.06.2017.
 */
public interface NebulaLdapService {
    NebulaUser getUser(String userDn, String password);
}
