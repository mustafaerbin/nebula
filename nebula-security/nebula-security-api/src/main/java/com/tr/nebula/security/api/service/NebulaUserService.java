package com.tr.nebula.security.api.service;

import com.tr.nebula.security.api.domain.NebulaUser;

import java.util.Optional;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public interface NebulaUserService {
    /**
     * Match login username pasword data and db/ldap username password data
     * @param username
     * @param password
     * @return
     */
    Optional<? extends NebulaUser> getUserByUsernameAndPassword(String username, String password);
}
