package com.tr.nebula.security.api.model;

import com.tr.nebula.security.api.domain.NebulaUser;

import java.util.List;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public interface SessionUser {
    /**
     *
     * @return session user
     */
    NebulaUser getUser();

    /**
     *
     * @return id of session user
     */
    Object getId();

    /**
     *
     * @return name of session user
     */
    String getUsername();

    /**
     *
     * @return authorities of session user
     */
    List getAuthorities();

    /**
     *
     * @return name of user
     */
    String getName();

    /**
     *
     * @return surname of user
     */
    String getSurname();
}
