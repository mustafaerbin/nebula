package com.tr.nebula.security.api.service;

import com.tr.nebula.security.api.model.SessionUser;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public interface NebulaSessionUserService {

    /**
     * Matches session user authorities and rest authorities
     *
     * @param sessionUser
     * @param authorities
     * @return
     */
    boolean hasMultiAuthority(SessionUser sessionUser, String... authorities);

    /**
     * Matches session user authorities and rest authorities as reverse
     *
     * @param sessionUser
     * @param authorities
     * @return
     */
    boolean hasReverseAuthority(SessionUser sessionUser, String... authorities);
}
